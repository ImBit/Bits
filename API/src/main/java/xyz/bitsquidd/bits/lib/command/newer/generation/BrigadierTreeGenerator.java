package xyz.bitsquidd.bits.lib.command.newer.generation;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.newer.BitsAnnotatedCommand;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Command;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Permission;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandMethodInfo;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandParameterInfo;
import xyz.bitsquidd.bits.lib.command.newer.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.newer.requirement.impl.PermissionRequirement;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NullMarked
public class BrigadierTreeGenerator {

    private static ArgumentBuilder<CommandSourceStack, ?> emptyLiteral() {
        return Commands.literal(""); //TODO see if this works!
    }

    public CommandNode<CommandSourceStack> createNode(final Class<? extends BitsAnnotatedCommand> commandClass) {
        return processCommandClass(commandClass).build();
    }

    // Returns a root, appended with all possible command branches
    // TODO get the defined variables as well. Use these for params.
    @SuppressWarnings("unchecked")
    public void processCommandClass(LiteralArgumentBuilder<CommandSourceStack> root, final Class<? extends BitsAnnotatedCommand> commandClass) {
        Command rootCommand = commandClass.getAnnotation(Command.class);
        if (rootCommand == null) throw new IllegalArgumentException("Command class must be annotated with @Command");

        String commandName = rootCommand.value();
        List<String> aliases = Arrays.asList(rootCommand.aliases());
        aliases.add(commandName);

        // Calculate requirements required for this branch
        List<BitsCommandRequirement> requirements = getRequirements(commandClass, commandName);
        root.requires(requirements);

        List<Class<?>> declaredClasses = Arrays.stream(commandClass.getDeclaredClasses()).toList();
        List<Method> declaredMethods = Arrays.stream(commandClass.getDeclaredMethods()).toList();
        // TODO: Add a generic default method if none are present - this could just be in bits command?


        for (String alias : aliases) {
            for (Class<?> nestedClass : declaredClasses) {
                if (BitsAnnotatedCommand.class.isAssignableFrom(nestedClass) && nestedClass.isAnnotationPresent(Command.class)) {
                    if (alias.isEmpty()) {
                        processCommandClass(root, (Class<? extends BitsAnnotatedCommand>)nestedClass);
                    } else {
                        processCommandClass(root.then(Commands.literal(alias)), (Class<? extends BitsAnnotatedCommand>)nestedClass);
                    }
                }
            }

            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Command.class)) {
                    BitsCommandMethodInfo methodInfo = new BitsCommandMethodInfo(method);
                    String methodName = methodInfo.getCommandName();

                    if (methodName.isEmpty()) {
                        getSourceStack(root, methodInfo);
                    } else {
                        getSourceStack(root.then(Commands.literal(methodName)), methodInfo);
                    }
                }
            }
        }
    }

    // Builds executions onto the root.
    private void getSourceStack(LiteralArgumentBuilder<CommandSourceStack> root, BitsCommandMethodInfo methodInfo) {
        List<BitsCommandParameterInfo> parameters = methodInfo.getParameters();

        if (parameters.isEmpty()) {
            root.executes(createCommandExecution(methodInfo));
        } else {
            List<ArgumentBuilder<CommandSourceStack, ?>> iterations = new ArrayList<>();

            for (BitsCommandParameterInfo parameter : methodInfo.getParameters()) {
                iterations.add(Commands.argument(parameter.getName(), getArgumentType(parameter.getType())));
            }

            // TODO switch to recursion as well.
            // Work backwards from the command stack, nesting each argument within the previous one.
            ArgumentBuilder<CommandSourceStack, ?> previous = iterations.getLast().executes(createCommandExecution(methodInfo));
            if (iterations.size() > 1) {
                for (int i = iterations.size() - 2; i >= 0; i--) {
                    previous = iterations.get(i).then(previous);
                }
            }

            root.then(previous);
        }
    }

    private com.mojang.brigadier.Command<CommandSourceStack> createCommandExecution(final BitsCommandMethodInfo methodInfo) {
        return ctx -> {
            BitsCommandContext bitsContext = new BitsCommandContext(ctx);

            List<@Nullable Object> arguments = new ArrayList<>();
            if (methodInfo.requiresContext()) arguments.add(bitsContext);

            for (BitsCommandParameterInfo parameter : methodInfo.getParameters()) {
                Object value;
                try {
                    value = ctx.getArgument(parameter.getName(), parameter.getRawType());
                } catch (IllegalArgumentException e) {
                    if (parameter.isOptional()) {
                        value = null; // If the argument isn't present and is optional, set to null
                    } else {
                        throw e; // If the argument just isn't present, we throw.
                    }
                }
                arguments.add(value);
            }


            Runnable commandExecution = () -> {
                try {
                    methodInfo.getMethod().invoke(arguments.toArray());
                } catch (Exception e) {
                    // TODO: throw a CommandException instead
                }
            };


            if (methodInfo.isAsync()) {
                Bukkit.getScheduler().runTaskAsynchronously(BitsConfig.getPlugin(), commandExecution);
            } else {
                Bukkit.getScheduler().runTask(BitsConfig.getPlugin(), commandExecution);
            }

            ctx.getSource().getSender().sendMessage("Command executed: " + methodInfo.getMethod().getName());
            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        };
    }


    ///  Requirements and permissions ///
    private List<BitsCommandRequirement> getRequirements(final Class<? extends BitsAnnotatedCommand> commandClass, final String commandName) {
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

    public String getDefaultPermissionString(final String commandName) {
        return BitsConfig.COMMAND_BASE_STRING + "." + commandName.replaceAll(" ", "_").toLowerCase();
    }

    //TODO implement static caching of requirement instances
    private BitsCommandRequirement getRequirementInstance(final @NotNull Class<? extends BitsCommandRequirement> requirementClass) {
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

    private ArgumentType<?> getArgumentType(final Type type) {
        return argumentRegistry.getArgumentType(type);
    }

}
