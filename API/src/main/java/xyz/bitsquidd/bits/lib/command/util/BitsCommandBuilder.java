package xyz.bitsquidd.bits.lib.command.util;

import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.BitsCommand;
import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.annotation.Permission;
import xyz.bitsquidd.bits.lib.command.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.requirement.BitsRequirementRegistry;
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
    private final Class<? extends BitsCommand> commandClass;

    @SuppressWarnings("FieldCanBeLocal")
    private final Command commandAnnotation;

    private final String commandName;
    private final List<String> commandAliases;
    private final String commandDescription;

    private final String permissionString;

    public BitsCommandBuilder(Class<? extends BitsCommand> commandClass) {
        this.commandClass = commandClass;

        commandAnnotation = commandClass.getAnnotation(Command.class);
        if (commandAnnotation == null) throw new CommandParseException("Class " + commandClass + " must be annotated with @Command");
        commandName = commandAnnotation.value();
        commandAliases = List.of(commandAnnotation.aliases());
        commandDescription = commandAnnotation.description();

        this.permissionString = BitsConfig.COMMAND_BASE_STRING + "." + commandName.replaceAll(" ", "_").toLowerCase();
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
            return List.of(constructor.getParameters());
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


    public List<BitsCommandRequirement> getRequirements() {
        List<BitsCommandRequirement> requirements = new ArrayList<>();
        if (commandName.isEmpty()) requirements.add(PermissionRequirement.of(permissionString));

        // Gather permission strings and convert them to requirements.
        Permission permissionAnnotation = commandClass.getAnnotation(Permission.class);
        if (permissionAnnotation != null) {
            requirements.addAll(Arrays.stream(permissionAnnotation.value())
                  .map(PermissionRequirement::of)
                  .toList());
        }

        // Gather requirement instances
        Requirement requirementAnnotation = commandClass.getAnnotation(Requirement.class);
        if (requirementAnnotation != null) {
            requirements.addAll(Arrays.stream(requirementAnnotation.value())
                  .map(clazz -> BitsRequirementRegistry.getInstance().getRequirement(clazz))
                  .toList());
        }

        return requirements;
    }

}
