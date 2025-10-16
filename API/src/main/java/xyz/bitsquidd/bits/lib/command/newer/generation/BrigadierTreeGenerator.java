package xyz.bitsquidd.bits.lib.command.newer.generation;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.newer.BitsAnnotatedCommand;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Command;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Permission;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.newer.info.CommandMethodInfo;
import xyz.bitsquidd.bits.lib.command.newer.info.ParameterInfo;
import xyz.bitsquidd.bits.lib.command.newer.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.newer.requirement.impl.PermissionRequirement;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NullMarked
public class BrigadierTreeGenerator {

    // Returns a root, appended with all possible command branches
    // TODO get the defined variables as well. Use these for params.
    @SuppressWarnings("unchecked")
    public ArgumentBuilder<CommandSourceStack, ?> processCommandClass(ArgumentBuilder<CommandSourceStack, ?> coreRoot, Class<? extends BitsAnnotatedCommand> commandClass) {
        Command rootCommand = commandClass.getAnnotation(Command.class);
        if (rootCommand == null) throw new IllegalArgumentException("Command class must be annotated with @Command");

        String commandName = rootCommand.value();
        List<String> aliases = Arrays.asList(rootCommand.aliases());
        aliases.add(commandName);

        // Calculate requirements required for this branch
        List<BitsCommandRequirement> requirements = getRequirements(commandClass, commandName);

        List<Class<?>> declaredClasses = Arrays.stream(commandClass.getDeclaredClasses()).toList();
        List<Method> declaredMethods = Arrays.stream(commandClass.getDeclaredMethods()).toList();
        // TODO: Add a generic default method if none are present - this could just be in bits command?


        for (String alias : aliases) {
            ArgumentBuilder<CommandSourceStack, ?> effectiveRoot;
            if (alias.isEmpty()) {
                effectiveRoot = coreRoot;
            } else {
                effectiveRoot = coreRoot.then(Commands.literal(alias));
            }

            for (Class<?> nestedClass : declaredClasses) {
                if (BitsAnnotatedCommand.class.isAssignableFrom(nestedClass) && nestedClass.isAnnotationPresent(Command.class)) {
                    effectiveRoot.then(processCommandClass(effectiveRoot, (Class<? extends BitsAnnotatedCommand>)nestedClass));
                }
            }

            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Command.class)) {
                    processCommandMethod(effectiveRoot, method);
                }
            }
        }

        return coreRoot;
    }

    // Builds executions onto the root.
    private void processCommandMethod(ArgumentBuilder<CommandSourceStack, ?> coreRoot, Method commandMethod) {
        CommandMethodInfo methodInfo = new CommandMethodInfo(commandMethod, null);
        List<ParameterInfo> parameters = methodInfo.getParameters();

        if (parameters.isEmpty()) {
            coreRoot.executes(createCommandExecution(methodInfo));
            return;
        }

        // Get name, add a then()
        // If no name, simply just return the command
    }


    ///  Requirements and permissions ///
    private List<BitsCommandRequirement> getRequirements(Class<? extends BitsAnnotatedCommand> commandClass, String commandName) {
        List<BitsCommandRequirement> requirements = new ArrayList<>();
        if (commandName.isEmpty()) requirements.add(new PermissionRequirement(getDefaultPermissionString(commandName)));

        // Gather permission strings
        Permission permissionAnnotation = commandClass.getAnnotation(Permission.class);
        if (permissionAnnotation != null) requirements.addAll(Arrays.stream(permissionAnnotation.value()).map(PermissionRequirement::new).toList());

        // Gather requirement instances
        Requirement requirementAnnotation = commandClass.getAnnotation(Requirement.class);
        if (requirementAnnotation != null) requirements.addAll(Arrays.stream(requirementAnnotation.value()).map(this::getRequirementInstance).toList());

        return requirements;
    }

    public String getDefaultPermissionString(String commandName) {
        return BitsConfig.COMMAND_BASE_STRING + "." + commandName.replaceAll(" ", "_").toLowerCase();
    }

    //TODO implement static caching of requirement instances
    private @NotNull BitsCommandRequirement getRequirementInstance(@NotNull Class<? extends BitsCommandRequirement> requirementClass) {
        return requirementInstances.computeIfAbsent(
              requirementClass, clazz -> {
                  try {
                      return clazz.getDeclaredConstructor().newInstance();
                  } catch (Exception e) {
                      throw new RuntimeException("Failed to create requirement instance: " + clazz.getName(), e);
                  }
              }
        );
    }


}
