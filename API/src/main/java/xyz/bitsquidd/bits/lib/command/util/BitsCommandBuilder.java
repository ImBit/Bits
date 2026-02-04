package xyz.bitsquidd.bits.lib.command.util;

import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.BitsCommand;
import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.annotation.Permission;
import xyz.bitsquidd.bits.lib.command.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.exception.CommandBuildException;
import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.requirement.impl.PermissionRequirement;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BitsCommandBuilder {
    private @Nullable BitsCommand commandInstance;
    private final Class<? extends BitsCommand> commandClass;
    private final boolean isStaticClass;

    @SuppressWarnings("FieldCanBeLocal")
    private final Command commandAnnotation;

    private final String commandName;
    private final List<String> commandAliases;
    private final String commandDescription;

    private final xyz.bitsquidd.bits.lib.permission.Permission corePermission;
    private final List<xyz.bitsquidd.bits.lib.permission.Permission> permissions = new ArrayList<>();

    // Allow us to build from an instance or a class.
    // Instances are used only for gathering extra requirements.
    public BitsCommandBuilder(@Nullable BitsCommand commandInstance) {
        this(Objects.requireNonNull(commandInstance).getClass());
        this.commandInstance = commandInstance;
        this.permissions.addAll(commandInstance.getAlternatePermissionStrings().stream().map(xyz.bitsquidd.bits.lib.permission.Permission::of).toList());
    }

    public BitsCommandBuilder(Class<? extends BitsCommand> commandClass) {
        this.commandClass = commandClass;
        this.isStaticClass = Modifier.isStatic(commandClass.getModifiers());

        commandAnnotation = commandClass.getAnnotation(Command.class);
        if (commandAnnotation == null) throw new CommandBuildException("Class " + commandClass + " must be annotated with @Command");
        commandName = commandAnnotation.value();
        commandAliases = List.of(commandAnnotation.aliases());
        commandDescription = commandAnnotation.description();

        this.corePermission = BitsConfig.get().getCommandManager().getCommandBasePermission().append("." + commandName.replaceAll(" ", "_").toLowerCase());
        this.permissions.add(corePermission);
    }


    public Class<? extends BitsCommand> getCommandClass() {
        return commandClass;
    }

    public String getCommandName() {
        return commandName;
    }

    public List<String> getCommandAliases() {
        return commandAliases;
    }

    public String getCommandDescription() {
        return commandDescription;
    }


    public List<Parameter> getParameters() {
        Constructor<?>[] constructors = commandClass.getConstructors();
        if (constructors.length > 0) {
            Constructor<?> constructor = constructors[0];
            return Arrays.stream(constructor.getParameters())
              .filter(param -> !param.isSynthetic() && !BitsCommand.class.isAssignableFrom(param.getType()))
              .toList();
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public List<Class<? extends BitsCommand>> getSubcommandClasses() {
        return Stream.of(commandClass.getDeclaredClasses())
          .filter(nestedClass -> BitsCommand.class.isAssignableFrom(nestedClass) && nestedClass.isAnnotationPresent(Command.class))
          .map(nestedClass -> (Class<? extends BitsCommand>)nestedClass)
          .collect(Collectors.toList());
    }

    public List<Method> getCommandMethods() {
        return Arrays.stream(commandClass.getDeclaredMethods())
          .filter(method -> method.isAnnotationPresent(Command.class))
          .toList();
    }

    public Constructor<?> toConstructor() {
        return commandClass.getDeclaredConstructors()[0];
    }


    public Set<BitsCommandRequirement> getRequirements() {
        Set<BitsCommandRequirement> requirements = new HashSet<>();
        if (commandName.isEmpty()) requirements.add(PermissionRequirement.of(permissions));

        // Gather permission strings and convert them to requirements.
        Permission permissionAnnotation = commandClass.getAnnotation(Permission.class);
        if (permissionAnnotation != null) {
            requirements.addAll(Arrays.stream(permissionAnnotation.value())
              .map(appended -> PermissionRequirement.of(xyz.bitsquidd.bits.lib.permission.Permission.of(corePermission + "." + appended)))
              .toList());
        }

        // Gather requirement instances
        Requirement requirementAnnotation = commandClass.getAnnotation(Requirement.class);
        if (requirementAnnotation != null) {
            requirements.addAll(Arrays.stream(requirementAnnotation.value())
              .map(clazz -> BitsConfig.get().getCommandManager().getRequirementRegistry().getRequirement(clazz))
              .toList());
        }

        if (commandInstance != null) requirements.addAll(commandInstance.getAddedRequirements());
        return requirements;
    }

    public xyz.bitsquidd.bits.lib.permission.Permission getCorePermission() {
        return corePermission;
    }

    public List<xyz.bitsquidd.bits.lib.permission.Permission> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }

    public boolean requiresOuterInstance() {
        return !isStaticClass && commandClass.isMemberClass();
    }

}
