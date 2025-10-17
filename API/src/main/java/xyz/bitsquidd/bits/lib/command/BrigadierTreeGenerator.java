package xyz.bitsquidd.bits.lib.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.ArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandBuilder;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandMethodInfo;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandParameterInfo;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO:
//  Bugs:
//   - Command annotations come before class parameters. e.g. teleport location player

@NullMarked
public class BrigadierTreeGenerator {
    public BrigadierTreeGenerator() {
    }

    public List<LiteralCommandNode<CommandSourceStack>> createNodes(BitsCommandBuilder commandBuilder) {
        LiteralArgumentBuilder<CommandSourceStack> dummyRoot = Commands.literal("dummy_root");
        processCommandClass(dummyRoot, commandBuilder, new ArrayList<>());

//        return dummyRoot.getArguments().stream()
//              .filter(node -> node instanceof LiteralCommandNode)
//              .map(node -> (LiteralCommandNode<CommandSourceStack>)node)
//              .toList();
        return List.of(dummyRoot.build());
    }

    // Returns a non-empty list of branches that will be built upon. This includes command aliases.
    // TODO look into PaperBrigadier copyLiteral
    private List<LiteralArgumentBuilder<CommandSourceStack>> createNextBranches(
          final BitsCommandBuilder commandBuilder,
          final @Nullable LiteralArgumentBuilder<CommandSourceStack> root
    ) {
        List<LiteralArgumentBuilder<CommandSourceStack>> commandBranches = new ArrayList<>();

        String commandName = commandBuilder.getCommandName();
        List<String> commandAliases = new ArrayList<>(commandBuilder.getCommandAliases());
        commandAliases.add(commandBuilder.getCommandName());

        // Note: Aliases are not created for commands without names.
        if (commandName.isEmpty()) {
            if (root == null) throw new CommandParseException("Root command class must have a name.");
            commandBranches.add(root);
            BitsConfig.getPlugin().getLogger().info("Registering command class without name: " + commandBuilder.getCommandName());
        } else {
            List<LiteralArgumentBuilder<CommandSourceStack>> nextBranches = commandAliases.stream().map(Commands::literal).toList();
            commandBranches.addAll(nextBranches);
            BitsConfig.getPlugin().getLogger().info("Registering command class: " + commandName + "  " + commandBuilder.getCommandName() + "  " + nextBranches);
        }

        return commandBranches;
    }

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

        // Create node children, don't attach until populated
        List<LiteralArgumentBuilder<CommandSourceStack>> nextBranches = createNextBranches(commandBuilder, branch);
        for (LiteralArgumentBuilder<CommandSourceStack> nextBranch : nextBranches) {
            for (Class<? extends BitsCommand> nestedClass : commandBuilder.getSubcommandClasses()) {
                processCommandClass(nextBranch, new BitsCommandBuilder(nestedClass), nonMutatedParameters);
            }

            for (Method method : commandBuilder.getCommandMethods()) {
                processCommandMethod(nextBranch, commandBuilder, method, nonMutatedParameters);
            }

            // Populate each child fully, then attach it to the parent
            if (!Objects.equals(nextBranch, branch)) branch.then(nextBranch);
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
                      Commands.argument(parameter.getName(), ArgumentRegistry.getInstance().getArgumentType(parameter.getType()))
//                            .suggests(ArgumentRegistry.getInstance().getParser(parameter.getType()).getSuggestionProvider())
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

                @SuppressWarnings("unchecked")
                AbstractArgumentParser<I, O> parser = (AbstractArgumentParser<I, O>)ArgumentRegistry.getInstance().getParser(parameter.getType());

                try {
                    value = parser.parse(ctx.getArgument(parameter.getName(), parser.getInputClass()), bitsCtx);
                } catch (IllegalArgumentException e) {
                    if (!parameter.isOptional()) throw new RuntimeException("Failed to get argument: " + parameter.getName(), e);
                    value = null;
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
                    throw new CommandParseException("Failed to execute command method: " + methodInfo.getMethod().getName() + " ", e);
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

}
