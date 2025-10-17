package xyz.bitsquidd.bits.lib.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// TODO:
//  Bugs:
//   - Command annotations come before class parameters. e.g. teleport location player

@NullMarked
public class BrigadierTreeGenerator {
    public BrigadierTreeGenerator() {
    }

    public List<LiteralCommandNode<CommandSourceStack>> createNodes(BitsCommandBuilder commandBuilder) {
        LiteralArgumentBuilder<CommandSourceStack> dummyRoot = Commands.literal("dummy_root"); // We create a "dummy_root" to be able to split core aliases.
        processCommandClass(dummyRoot, commandBuilder, new ArrayList<>());

        return dummyRoot.getArguments().stream()
              .filter(node -> node instanceof LiteralCommandNode)
              .map(node -> (LiteralCommandNode<CommandSourceStack>)node)
              .toList();
    }

    // Returns a non-empty list of branches that will be built upon. This includes command aliases.
    // TODO look into PaperBrigadier copyLiteral
    private List<ArgumentBuilder<CommandSourceStack, ?>> createNextBranches(
          final BitsCommandBuilder commandBuilder,
          final @Nullable ArgumentBuilder<CommandSourceStack, ?> root
    ) {
        List<ArgumentBuilder<CommandSourceStack, ?>> commandBranches = new ArrayList<>();

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
          final ArgumentBuilder<CommandSourceStack, ?> branch,
          final BitsCommandBuilder commandBuilder,
          final List<BitsCommandParameterInfo> addedParameters
    ) {
        // Calculate requirements required for this branch
        branch.requires(ctx -> commandBuilder.getRequirements().stream()
              .allMatch(requirement -> requirement.test(new BitsCommandContext(ctx)))
        );

        // Create parameters needed for this class.
        //TODO consider having documentation on which constructor is used
        List<BitsCommandParameterInfo> newParameters = commandBuilder.getParameters().stream().map(BitsCommandParameterInfo::new).toList();
        List<BitsCommandParameterInfo> nonMutatedParameters = new ArrayList<>(addedParameters);
        nonMutatedParameters.addAll(newParameters);

        // Create next branches with aliases
        List<ArgumentBuilder<CommandSourceStack, ?>> nextBranches = createNextBranches(commandBuilder, branch);


        nextBranches.forEach(nextBranch -> {
            List<ArgumentBuilder<CommandSourceStack, ?>> addedParamBranches = newParameters.stream().map(param -> Commands.argument(
                  param.getName(),
                  ArgumentRegistry.getInstance().getArgumentType(param.getType())
            )).collect(Collectors.toList());

            ArgumentBuilder<CommandSourceStack, ?> workingBranch = nextBranch;
            if (!addedParamBranches.isEmpty()) workingBranch = addedParamBranches.getLast();

            for (Class<? extends BitsCommand> nestedClass : commandBuilder.getSubcommandClasses()) {
                processCommandClass(workingBranch, new BitsCommandBuilder(nestedClass), nonMutatedParameters);
            }

            for (Method method : commandBuilder.getCommandMethods()) {
                processCommandMethod(workingBranch, commandBuilder, method, nonMutatedParameters);
            }

            // Populate each child fully, then attach it to the parent
            if (!Objects.equals(workingBranch, nextBranch)) nextBranch.then(buildBackward(addedParamBranches));
            if (!Objects.equals(nextBranch, branch)) branch.then(buildBackward(List.of(nextBranch)));
        });
    }

    private ArgumentBuilder<CommandSourceStack, ?> buildBackward(List<ArgumentBuilder<CommandSourceStack, ?>> toAdd) {
        if (toAdd.isEmpty()) throw new CommandParseException("No more branches to add.");
        if (toAdd.size() == 1) return toAdd.getFirst();

        ArgumentBuilder<CommandSourceStack, ?> first = toAdd.getFirst();
        List<ArgumentBuilder<CommandSourceStack, ?>> rest = toAdd.subList(1, toAdd.size());
        first.then(buildBackward(new ArrayList<>(rest)));
        return first;
    }

    // Builds executions onto the root.
    private void processCommandMethod(
          final ArgumentBuilder<CommandSourceStack, ?> branch,
          final BitsCommandBuilder commandBuilder,
          final Method method,
          final List<BitsCommandParameterInfo> addedParameters
    ) {
        BitsCommandMethodInfo methodInfo = new BitsCommandMethodInfo(method, addedParameters);

        if (methodInfo.getMethodParameters().isEmpty()) {
            BitsConfig.getPlugin().getLogger().info("Registering command method without parameters: " + method.getName() + "  " + methodInfo.getMethodParameters());
            branch.executes(createCommandExecution(commandBuilder, methodInfo));
        } else {
            BitsConfig.getPlugin().getLogger().info("Registering command method with parameters: " + method.getName() + "  " + methodInfo.getMethodParameters());
            branch.then(addParameters(commandBuilder, methodInfo, methodInfo.getMethodParameters()));
        }
    }

    private RequiredArgumentBuilder<CommandSourceStack, ?> addParameters(
          final BitsCommandBuilder commandBuilder,
          final BitsCommandMethodInfo methodInfo,
          final List<BitsCommandParameterInfo> parameters
    ) {
        if (parameters.isEmpty()) throw new CommandParseException("No parameters to add to command branch.");

        if (parameters.size() == 1) {
            BitsCommandParameterInfo last = new ArrayList<>(parameters).removeFirst();
            return Commands.argument(last.getName(), ArgumentRegistry.getInstance().getArgumentType(last.getType()))
                  .executes(createCommandExecution(commandBuilder, methodInfo));
        }

        List<BitsCommandParameterInfo> paramsCopy = new ArrayList<>(parameters);
        BitsCommandParameterInfo first = paramsCopy.removeFirst();

        RequiredArgumentBuilder<CommandSourceStack, ?> nextBranch = addParameters(commandBuilder, methodInfo, paramsCopy);
        return Commands.argument(first.getName(), ArgumentRegistry.getInstance().getArgumentType(first.getType())).then(nextBranch);
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

            for (BitsCommandParameterInfo parameter : methodInfo.getAllParameters()) {
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
