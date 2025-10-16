package xyz.bitsquidd.bits.lib.command.newer;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.newer.annotation.Command;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Permission;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentTypeRegistry;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandMethodInfo;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandParameterInfo;
import xyz.bitsquidd.bits.lib.command.newer.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.newer.requirement.impl.PermissionRequirement;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;

@NullMarked
public class BrigadierTreeGenerator {
    private final ArgumentTypeRegistry argumentRegistry;
    private final Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> requirementInstances;

    public BrigadierTreeGenerator(
          ArgumentTypeRegistry argumentRegistry, Map<Class<? extends BitsCommandRequirement>,
                BitsCommandRequirement> requirementInstances
    ) {
        this.argumentRegistry = argumentRegistry;
        this.requirementInstances = requirementInstances;
    }


    //TODO allow for alias support here! - dont have repeated code with processing the class.
    public LiteralCommandNode<CommandSourceStack> createNode(final Class<? extends BitsAnnotatedCommand> commandClass) {
        Command rootCommand = commandClass.getAnnotation(Command.class);
        if (rootCommand == null) throw new IllegalArgumentException("Command class must be annotated with @Command");
        String commandName = rootCommand.value();

        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(commandName);
        processCommandClass(root, commandClass, new ArrayList<>());

        return root.build();
    }

    // Returns a root, appended with all possible command branches
    // TODO get the defined variables as well. Use these for params.
    @SuppressWarnings("unchecked")
    private void processCommandClass(LiteralArgumentBuilder<CommandSourceStack> root, final Class<? extends BitsAnnotatedCommand> commandClass, final List<Parameter> addedParameters) {
        Command commandAnnotation = commandClass.getAnnotation(Command.class);
        if (commandAnnotation == null) throw new IllegalArgumentException("Command class must be annotated with @Command");

        String commandName = commandAnnotation.value();
        List<String> aliases = Arrays.asList(commandAnnotation.aliases());
        aliases.add(commandName);

        // Calculate requirements required for this branch
        List<BitsCommandRequirement> requirements = getRequirements(commandClass, commandName);
        root.requires(ctx -> requirements.stream()
              .allMatch(requirement -> requirement.test(new BitsCommandContext(ctx)))
        );

        //TODO consider having documentation on which constructor is used
        Constructor<?>[] constructors = commandClass.getConstructors();
        Constructor<?> constructor = constructors[0];
        List<Parameter> nonMutatedParameters = new ArrayList<>(addedParameters);
        nonMutatedParameters.addAll(Arrays.stream(constructor.getParameters()).toList());

        List<Class<?>> declaredClasses = Arrays.stream(commandClass.getDeclaredClasses()).toList();
        List<Method> declaredMethods = Arrays.stream(commandClass.getDeclaredMethods()).toList();
        // TODO: Add a generic default method if none are present - this could just be in bits command?


        for (String alias : aliases) {
            for (Class<?> nestedClass : declaredClasses) {
                if (BitsAnnotatedCommand.class.isAssignableFrom(nestedClass) && nestedClass.isAnnotationPresent(Command.class)) {
                    if (alias.isEmpty()) {
                        processCommandClass(root, (Class<? extends BitsAnnotatedCommand>)nestedClass, nonMutatedParameters);
                    } else {
                        processCommandClass(root.then(Commands.literal(alias)), (Class<? extends BitsAnnotatedCommand>)nestedClass, nonMutatedParameters);
                    }
                }
            }

            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Command.class)) {
                    BitsCommandMethodInfo methodInfo = new BitsCommandMethodInfo(method, nonMutatedParameters);
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
            List<RequiredArgumentBuilder<CommandSourceStack, ?>> iterations = new ArrayList<>();

            for (BitsCommandParameterInfo parameter : methodInfo.getParameters()) {
                iterations.add(Commands.argument(parameter.getName(), getArgumentType(parameter.getType())));
            }

            // TODO switch to recursion as well.
            // Work backwards from the command stack, nesting each argument within the previous one.
            RequiredArgumentBuilder<CommandSourceStack, ?> previous = iterations.getLast().executes(createCommandExecution(methodInfo));
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
            BitsCommandContext bitsContext = new BitsCommandContext(ctx.getSource());

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

    private String getDefaultPermissionString(final String commandName) {
        return BitsConfig.COMMAND_BASE_STRING + "." + commandName.replaceAll(" ", "_").toLowerCase();
    }

    //TODO implement static caching of requirement instances
    private BitsCommandRequirement getRequirementInstance(final Class<? extends BitsCommandRequirement> requirementClass) {
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
        return Objects.requireNonNull(argumentRegistry.getArgumentType(type), "Argument type not registered: " + type.getTypeName());
    }

}
