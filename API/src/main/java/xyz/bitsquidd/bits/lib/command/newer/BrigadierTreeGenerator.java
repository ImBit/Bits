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

import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentTypeRegistry;
import xyz.bitsquidd.bits.lib.command.newer.arg.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.newer.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandBuilder;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandMethodInfo;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandParameterInfo;
import xyz.bitsquidd.bits.lib.command.newer.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// TODO:
//  Bugs:
//   - Command annotations come before class parameters. e.g. teleport location player

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
    public LiteralCommandNode<CommandSourceStack> createNode(BitsCommandBuilder commandBuilder) {
        LiteralArgumentBuilder<CommandSourceStack> root = createNextBranch(commandBuilder, null);
        processCommandClass(root, commandBuilder, new ArrayList<>());

        return root.build();
    }

    private LiteralArgumentBuilder<CommandSourceStack> createNextBranch(
          final BitsCommandBuilder commandBuilder,
          final @Nullable LiteralArgumentBuilder<CommandSourceStack> root
    ) {
        String commandName = commandBuilder.getCommandName();
        LiteralArgumentBuilder<CommandSourceStack> nextBranch;
        if (commandName.isEmpty()) {
            if (root == null) throw new CommandParseException("Root command class must have a name.");
            nextBranch = root;
            BitsConfig.getPlugin().getLogger().info("Registering command class without name: " + commandBuilder.getCommandName());
        } else {
            nextBranch = Commands.literal(commandName);
            if (root != null) root.then(nextBranch);
            BitsConfig.getPlugin().getLogger().info("Registering command class: " + commandName + "  " + commandBuilder.getCommandName());
        }

        return nextBranch;
    }

    // TODO get the defined variables as well. Use these for params.
    private void processCommandClass(
          final LiteralArgumentBuilder<CommandSourceStack> branch,
          final BitsCommandBuilder commandBuilder,
          final List<Parameter> addedParameters
    ) {
        // Calculate requirements required for this branch
        branch.requires(ctx -> commandBuilder.getRequirements().stream()
              .allMatch(requirement -> requirement.test(new BitsCommandContext(ctx)))
        );

        //TODO consider having documentation on which constructor is used
        List<Parameter> nonMutatedParameters = new ArrayList<>(addedParameters);
        nonMutatedParameters.addAll(commandBuilder.getParameters());

        // TODO: Add a generic default method if none are present - this could just be in bits command?
        // TODO add alias support

        for (Class<?> nestedClass : commandBuilder.getSubcommandClasses()) {
            processCommandClass(createNextBranch(commandBuilder, branch), commandBuilder, nonMutatedParameters);
        }

        for (Method method : commandBuilder.getCommandMethods()) {
            processCommandMethod(branch, commandBuilder, method, nonMutatedParameters);
        }
    }

    // Builds executions onto the root.
    private void processCommandMethod(
          final LiteralArgumentBuilder<CommandSourceStack> branch,
          final BitsCommandBuilder commandBuilder,
          final Method method,
          final List<Parameter> addedParameters
    ) {
        BitsCommandMethodInfo methodInfo = new BitsCommandMethodInfo(method, addedParameters);
        List<BitsCommandParameterInfo> parameters = methodInfo.getParameters();

        if (parameters.isEmpty()) {
            BitsConfig.getPlugin().getLogger().info("Registering command method without parameters: " + method.getName() + "  " + methodInfo.getParameters());
            branch.executes(createCommandExecution(commandBuilder, methodInfo));
        } else {
            BitsConfig.getPlugin().getLogger().info("Registering command method with parameters: " + method.getName() + "  " + methodInfo.getParameters());
            List<RequiredArgumentBuilder<CommandSourceStack, ?>> iterations = new ArrayList<>();

            for (BitsCommandParameterInfo parameter : methodInfo.getParameters()) {
                iterations.add(
                      Commands.argument(parameter.getName(), getArgumentType(parameter.getType()))
                            .suggests(argumentRegistry.getParser(parameter.getType()).getSuggestionProvider())
                );
            }

            // TODO switch to recursion as well.
            // Work backwards from the command stack, nesting each argument within the previous one.
            RequiredArgumentBuilder<CommandSourceStack, ?> previous = iterations.getLast().executes(createCommandExecution(commandBuilder, methodInfo));
            if (iterations.size() > 1) {
                for (int i = iterations.size() - 2; i >= 0; i--) {
                    previous = iterations.get(i).then(previous);
                }
            }

            branch.then(previous);
        }
    }

    private <I, O> com.mojang.brigadier.Command<CommandSourceStack> createCommandExecution(
          final BitsCommandBuilder commandBuilder,
          final BitsCommandMethodInfo methodInfo
    ) {
        return ctx -> {
            final BitsCommandContext bitsCtx = new BitsCommandContext(ctx.getSource());

            // Create the list of arguments needed to call the method.
            final List<@Nullable Object>[] allArguments = new List[]{new ArrayList<>()};
            if (methodInfo.requiresContext()) allArguments[0].add(bitsCtx);

            for (BitsCommandParameterInfo parameter : methodInfo.getParameters()) {
                Object value;

                AbstractArgumentParser<I, O> parser = (AbstractArgumentParser<I, O>)argumentRegistry.getParser(parameter.getType());

                try {
                    value = parser.parse(ctx.getArgument(parameter.getName(), parser.getInputClass()), bitsCtx);
                } catch (IllegalArgumentException e) {
                    if (parameter.isOptional()) {
                        value = null;
                    } else {
                        throw new RuntimeException("Failed to get argument: " + parameter.getName(), e);
                    }
                }
                allArguments[0].add(value);
            }

            Runnable commandExecution = () -> {
                try {
                    Constructor<?> constructor = commandBuilder.toConstructor();
                    int constructorParamCount = constructor.getParameterCount();
                    Object instance;

                    if (constructorParamCount == 0) {
                        instance = constructor.newInstance();
                    } else {
                        int startIndex = methodInfo.requiresContext() ? 1 : 0;

                        Object[] constructorArgs = allArguments[0].subList(startIndex, startIndex + constructorParamCount).toArray();
                        instance = constructor.newInstance(constructorArgs);

                        List<@Nullable Object> methodArguments = new ArrayList<>();
                        if (methodInfo.requiresContext()) methodArguments.add(allArguments[0].getFirst());

                        methodArguments.addAll(allArguments[0].subList(startIndex + constructorParamCount, allArguments[0].size()));
                        allArguments[0] = methodArguments;
                    }

                    methodInfo.getMethod().invoke(instance, allArguments[0].toArray());

                } catch (Exception e) {
                    throw new CommandParseException("Failed to execute command method: " + methodInfo.getMethod().getName());
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


    private ArgumentType<?> getArgumentType(final Type type) {
        return Objects.requireNonNull(argumentRegistry.getArgumentType(type), "Argument type not registered: " + type.getTypeName());
    }

}
