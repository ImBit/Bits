package xyz.bitsquidd.bits.lib.command.info;

import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.BitsAnnotatedCommand;
import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.annotation.Permission;
import xyz.bitsquidd.bits.lib.command.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.requirement.impl.PermissionRequirement;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NullMarked
public class BitsCommandBuilder {
    private final Class<? extends BitsAnnotatedCommand> commandClass;

    private final Command commandAnnotation;

    private final String commandName;
    private final List<String> commandAliases;
    private final String commandDescription;

    private final String permissionString;

    public BitsCommandBuilder(Class<? extends BitsAnnotatedCommand> commandClass) {
        this.commandClass = commandClass;

        commandAnnotation = commandClass.getAnnotation(Command.class);
        if (commandAnnotation == null) throw new CommandParseException("Class " + commandClass + " must be annotated with @Command");
        commandName = commandAnnotation.value();
        commandAliases = List.of(commandAnnotation.aliases());
        commandDescription = commandAnnotation.description();

        this.permissionString = BitsConfig.COMMAND_BASE_STRING + "." + commandName.replaceAll(" ", "_").toLowerCase();
    }


    public Class<? extends BitsAnnotatedCommand> getCommandClass() {
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
            return List.of(constructor.getParameters());
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public List<Class<? extends BitsAnnotatedCommand>> getSubcommandClasses() {
        return Stream.of(commandClass.getDeclaredClasses())
              .filter(nestedClass -> BitsAnnotatedCommand.class.isAssignableFrom(nestedClass) && nestedClass.isAnnotationPresent(Command.class))
              .map(nestedClass -> (Class<? extends BitsAnnotatedCommand>)nestedClass)
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


    public List<BitsCommandRequirement> getRequirements() {
        List<BitsCommandRequirement> requirements = new ArrayList<>();
        if (commandName.isEmpty()) requirements.add(new PermissionRequirement(permissionString));

        // Gather permission strings
        Permission permissionAnnotation = commandClass.getAnnotation(Permission.class);
        if (permissionAnnotation != null) requirements.addAll(Arrays.stream(permissionAnnotation.value()).map(PermissionRequirement::new).toList());

        // Gather requirement instances
        Requirement requirementAnnotation = commandClass.getAnnotation(Requirement.class);
        if (requirementAnnotation != null) requirements.addAll(Arrays.stream(requirementAnnotation.value()).map(this::getRequirementInstance).toList());

        return requirements;
    }

    //TODO implement static caching of requirement instances
    private BitsCommandRequirement getRequirementInstance(final Class<? extends BitsCommandRequirement> requirementClass) {
        return requirementInstances.computeIfAbsent(
              requirementClass, clazz -> {
                  try {
                      return clazz.getDeclaredConstructor().newInstance();
                  } catch (Exception e) {
                      throw new RuntimeException("Failed to create requirement instance.", e);
                  }
              }
        );
    }

}
