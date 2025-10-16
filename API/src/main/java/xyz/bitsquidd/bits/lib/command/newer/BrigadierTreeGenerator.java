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
    public LiteralCommandNode<CommandSourceStack> createNode(final BitsAnnotatedCommand commandInstance) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("bitsCommand");
        processCommandClass(root, commandInstance, new ArrayList<>());

        return root.build();
    }

    // Returns a root, appended with all possible command branches
    // TODO get the defined variables as well. Use these for params.
    @SuppressWarnings("unchecked")
    private void processCommandClass(
          final LiteralArgumentBuilder<CommandSourceStack> root,
          final BitsAnnotatedCommand commandInstance,
          final List<Parameter> addedParameters
    ) {
        Command commandAnnotation = commandInstance.getClass().getAnnotation(Command.class);
        if (commandAnnotation == null) throw new IllegalArgumentException("Command class must be annotated with @Command");

        String commandName = commandAnnotation.value();
        @Nullable LiteralArgumentBuilder<CommandSourceStack> nextBranch;
        if (commandName.isEmpty()) {
            nextBranch = null;
            BitsConfig.getPlugin().getLogger().info("Registering command class without name: " + commandInstance.getClass().getName());
        } else {
            nextBranch = Commands.literal(commandName);
            BitsConfig.getPlugin().getLogger().info("Registering command class: " + commandName + "  " + commandInstance.getClass().getName());
        }


        // Calculate requirements required for this branch
        List<BitsCommandRequirement> requirements = getRequirements(commandInstance, commandName);
        root.requires(ctx -> requirements.stream()
              .allMatch(requirement -> requirement.test(new BitsCommandContext(ctx)))
        );

        //TODO consider having documentation on which constructor is used
        List<Parameter> nonMutatedParameters = new ArrayList<>(addedParameters);
        Constructor<?>[] constructors = commandInstance.getClass().getConstructors();
        if (constructors.length > 0) {
            Constructor<?> constructor = constructors[0];
            nonMutatedParameters.addAll(Arrays.stream(constructor.getParameters()).toList());
        }

        List<Class<?>> declaredClasses = Arrays.stream(commandInstance.getClass().getDeclaredClasses()).toList();
        List<Method> declaredMethods = Arrays.stream(commandInstance.getClass().getDeclaredMethods()).toList();
        // TODO: Add a generic default method if none are present - this could just be in bits command?

        // TODO add alias support

        for (Class<?> nestedClass : declaredClasses) {
            if (BitsAnnotatedCommand.class.isAssignableFrom(nestedClass) && nestedClass.isAnnotationPresent(Command.class)) {
                try {
                    processCommandClass(nextBranch != null ? nextBranch : root, (BitsAnnotatedCommand)nestedClass.getDeclaredConstructor().newInstance(), nonMutatedParameters);
                } catch (Exception e) {
                    BitsConfig.getPlugin().getLogger().severe("No default constructor found for command class: " + nestedClass.getName());
                }
            }
        }

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Command.class)) {
                processCommandMethod(nextBranch != null ? nextBranch : root, commandInstance, method, nonMutatedParameters);
            }
        }

        if (nextBranch != null) root.then(nextBranch);
    }

    // Builds executions onto the root.
    private void processCommandMethod(final LiteralArgumentBuilder<CommandSourceStack> root, final BitsAnnotatedCommand commandInstance, final Method method, final List<Parameter> addedParameters) {
        Command commandAnnotation = method.getAnnotation(Command.class);
        if (commandAnnotation == null) throw new IllegalArgumentException("Command method must be annotated with @Command");

        String commandName = commandAnnotation.value();
        @Nullable LiteralArgumentBuilder<CommandSourceStack> nextBranch;
        if (commandName.isEmpty()) {
            nextBranch = null;
            BitsConfig.getPlugin().getLogger().info("Registering command class without name: " + commandName);
        } else {
            nextBranch = Commands.literal(commandName);
            BitsConfig.getPlugin().getLogger().info("Registering command class: " + commandName + "  " + commandName);
        }

        BitsCommandMethodInfo methodInfo = new BitsCommandMethodInfo(method, addedParameters);


        List<BitsCommandParameterInfo> parameters = methodInfo.getParameters();

        if (parameters.isEmpty()) {
            BitsConfig.getPlugin().getLogger().info("Registering command method without parameters: " + method.getName() + "  " + methodInfo.getParameters());
            (nextBranch != null ? nextBranch : root).executes(createCommandExecution(commandInstance, methodInfo));
        } else {
            BitsConfig.getPlugin().getLogger().info("Registering command method with parameters: " + method.getName() + "  " + methodInfo.getParameters());
            List<RequiredArgumentBuilder<CommandSourceStack, ?>> iterations = new ArrayList<>();

            for (BitsCommandParameterInfo parameter : methodInfo.getParameters()) {
                iterations.add(Commands.argument(parameter.getName(), getArgumentType(parameter.getType())));
            }

            // TODO switch to recursion as well.
            // Work backwards from the command stack, nesting each argument within the previous one.
            RequiredArgumentBuilder<CommandSourceStack, ?> previous = iterations.getLast().executes(createCommandExecution(commandInstance, methodInfo));
            if (iterations.size() > 1) {
                for (int i = iterations.size() - 2; i >= 0; i--) {
                    previous = iterations.get(i).then(previous);
                }
            }

            (nextBranch != null ? nextBranch : root).then(previous);
        }

        if (nextBranch != null) root.then(nextBranch);
    }

    private com.mojang.brigadier.Command<CommandSourceStack> createCommandExecution(final BitsAnnotatedCommand commandInstance, final BitsCommandMethodInfo methodInfo) {
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
                BitsConfig.getPlugin().getLogger().info("here1");

                try {
                    BitsConfig.getPlugin().getLogger().info("here2");
                    methodInfo.getMethod().invoke(commandInstance, arguments.toArray());
                } catch (Exception e) {
                    BitsConfig.getPlugin().getLogger().info("stucture: " + methodInfo.getMethod());
                    BitsConfig.getPlugin().getLogger().info("THERE IS AN EXCEPTION!: " + e.getMessage());
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
    private List<BitsCommandRequirement> getRequirements(final BitsAnnotatedCommand commandInstance, final String commandName) {
        List<BitsCommandRequirement> requirements = new ArrayList<>();
        if (commandName.isEmpty()) requirements.add(new PermissionRequirement(getDefaultPermissionString(commandName)));

        // Gather permission strings
        Permission permissionAnnotation = commandInstance.getClass().getAnnotation(Permission.class);
        if (permissionAnnotation != null) requirements.addAll(Arrays.stream(permissionAnnotation.value()).map(PermissionRequirement::new).toList());

        // Gather requirement instances
        Requirement requirementAnnotation = commandInstance.getClass().getAnnotation(Requirement.class);
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
