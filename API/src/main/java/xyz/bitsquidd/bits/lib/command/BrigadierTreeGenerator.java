package xyz.bitsquidd.bits.lib.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.argument.BrigadierArgumentMapping;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.debugging.TreeDebugger;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.nms.Commands;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandBuilder;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.util.CommandMethodInfo;
import xyz.bitsquidd.bits.lib.command.util.CommandParameterInfo;
import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.lib.type.GreedyString;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NullMarked
public class BrigadierTreeGenerator {
    private final BitsCommandManager bitsCommandManager = BitsConfig.getCommandManager();

    public BrigadierTreeGenerator() {
    }

    public List<LiteralCommandNode<CommandSourceStack>> createNodes(
          BitsCommandBuilder commandBuilder
    ) {
        LiteralArgumentBuilder<CommandSourceStack> dummyRoot = Commands.literal("dummy_root"); // We create a "dummy_root" to be able to split core aliases.
        processCommandClass(dummyRoot, commandBuilder, new ArrayList<>());

        List<LiteralCommandNode<CommandSourceStack>> nodes = dummyRoot.getArguments().stream()
              .filter(node -> node instanceof LiteralCommandNode)
              .map(node -> (LiteralCommandNode<CommandSourceStack>)node)
              .toList();

        if (BitsConfig.isDevelopmentMode()) BitsConfig.getLogger().info(TreeDebugger.visualizeCommandTree(nodes));
        return nodes;
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
        commandAliases.add(commandName);

        // Note: Aliases are not created for commands without names.
        if (commandName.isEmpty()) {
            if (root == null) throw new CommandParseException("Root command class must have a name.");
            commandBranches.add(root);
        } else {
            List<LiteralArgumentBuilder<CommandSourceStack>> nextBranches = commandAliases.stream().map(Commands::literal).toList();
            commandBranches.addAll(nextBranches);
        }

        return commandBranches;
    }

    private void processCommandClass(
          final ArgumentBuilder<CommandSourceStack, ?> branch,
          final BitsCommandBuilder commandBuilder,
          final List<CommandParameterInfo> addedParameters
    ) {
        // Calculate requirements required for this branch
        branch.requires(ctx -> commandBuilder.getRequirements().stream()
              .allMatch(requirement -> requirement.test(bitsCommandManager.createSourceContext(ctx)))
        );

        // Create parameters needed for this class.
        List<CommandParameterInfo> classParameters = commandBuilder.getParameters().stream().map(CommandParameterInfo::new).toList();
        List<CommandParameterInfo> nonMutatedParameters = new ArrayList<>(addedParameters);
        nonMutatedParameters.addAll(classParameters);

        // Create next branches with aliases
        List<ArgumentBuilder<CommandSourceStack, ?>> nextBranches = createNextBranches(commandBuilder, branch);

        nextBranches.forEach(nextBranch -> {
            List<ArgumentBuilder<CommandSourceStack, ?>> addedParamBranches = new ArrayList<>();

            // Add brigadier branches for all the new parameters
            classParameters.forEach(param -> {
                addedParamBranches.addAll(param.createBrigadierArguments());
            });

            ArgumentBuilder<CommandSourceStack, ?> workingBranch = nextBranch;
            if (!addedParamBranches.isEmpty()) workingBranch = addedParamBranches.getLast();

            for (Class<? extends BitsCommand> nestedClass : commandBuilder.getSubcommandClasses()) {
                processCommandClass(workingBranch, new BitsCommandBuilder(nestedClass), nonMutatedParameters);
            }

            for (Method method : commandBuilder.getCommandMethods()) {
                if (!Modifier.isPublic(method.getModifiers())) {
                    BitsConfig.getLogger().warn("Skipping non-public command method: " + method.getName() + " for command: " + commandBuilder.getCommandName());
                    continue;
                }
                processCommandMethod(workingBranch, commandBuilder, new CommandMethodInfo(method, nonMutatedParameters));
            }

            // Populate each child fully, then attach it to the parent
            if (!Objects.equals(workingBranch, nextBranch)) nextBranch.then(buildBackward(addedParamBranches));
            if (!Objects.equals(nextBranch, branch)) branch.then(buildBackward(List.of(nextBranch)));
        });
    }


    // Builds executions onto the root.
    private void processCommandMethod(
          final ArgumentBuilder<CommandSourceStack, ?> nextBranch,
          final BitsCommandBuilder commandBuilder,
          final CommandMethodInfo methodInfo
    ) {
        // Add all extra parameters to the branch.
        List<ArgumentBuilder<CommandSourceStack, ?>> paramBranch = new ArrayList<>();

        // Add literal name if it exists.
        // Note we currently don't support aliases for method literals.
        if (!methodInfo.literalName().isEmpty()) {
            paramBranch.add(Commands.literal(methodInfo.literalName()));
        }

        methodInfo.getMethodParameters().forEach(param -> {
            paramBranch.addAll(param.createBrigadierArguments());
        });

        ArgumentBuilder<CommandSourceStack, ?> workingBranch = nextBranch;
        if (!paramBranch.isEmpty()) workingBranch = paramBranch.getLast();

        // Add method requirements
        workingBranch.requires(ctx -> methodInfo.getRequirements(commandBuilder.getPermissionString()).stream()
              .allMatch(requirement -> requirement.test(bitsCommandManager.createSourceContext(ctx)))
        );
        workingBranch.executes(createCommandExecution(commandBuilder, methodInfo));

        if (!Objects.equals(workingBranch, nextBranch)) nextBranch.then(buildBackward(paramBranch));
    }


    // Creates a command execution when no more parameters need to be added.
    @SuppressWarnings("NullableProblems")
    private Command<CommandSourceStack> createCommandExecution(
          final BitsCommandBuilder commandBuilder,
          final CommandMethodInfo methodInfo
    ) {
        return ctx -> {
            final BitsCommandContext bitsCtx = bitsCommandManager.createContext(ctx);

            // Create the list of arguments needed to call the method.
            ArrayList<@Nullable Object> parsedArguments = new ArrayList<>();

            for (CommandParameterInfo parameter : methodInfo.getAllParameters()) {
                AbstractArgumentParser<?> parser = parameter.getParser();

                // Collect primitive objects for the parameter
                ArrayList<@Nullable Object> primitiveObjects = new ArrayList<>();
                for (int i = 0; i < parser.getInputTypes().size(); i++) {
                    BrigadierArgumentMapping holder = parameter.getHeldArguments().get(i);

                    Object primitiveValue;
                    try {
                        Class<?> checkType = holder.typeSignature().toRawType();
                        if (checkType == GreedyString.class) checkType = String.class; // Special case for greedy strings as they require a GreedyString input, but parse into String

                        primitiveValue = ctx.getArgument(holder.argumentName(), checkType);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Failed to get argument: " + parameter, e);
                    }

                    primitiveObjects.add(primitiveValue);
                }

                Object value;
                try {
                    if (primitiveObjects.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("One or more primitive arguments are null.");

                    value = BitsArgumentRegistry.getInstance().parseArguments(parser, primitiveObjects, BitsConfig.getCommandManager().createContext(ctx));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Failed to get argument: " + parameter, e);
                }

                parsedArguments.add(value);
            }


            // Execute the command with the required, parsed arguments
            Runnable commandExecution = () -> {
                try {
                    Constructor<?> commandClass = commandBuilder.toConstructor();
                    int constructorParamCount = commandClass.getParameterCount();
                    Object instance;

                    List<@Nullable Object> methodArguments = new ArrayList<>();

                    if (constructorParamCount == 0) {
                        instance = commandClass.newInstance();
                        methodArguments = parsedArguments;
                    } else {
                        Object[] constructorArgs = parsedArguments.subList(0, constructorParamCount).toArray();
                        instance = commandClass.newInstance(constructorArgs);

                        if (constructorParamCount < parsedArguments.size()) {
                            methodArguments = new ArrayList<>(parsedArguments.subList(constructorParamCount, parsedArguments.size()));
                        }
                    }

                    if (methodInfo.requiresContext()) methodArguments.addFirst(bitsCtx);

                    methodInfo.getMethod().invoke(instance, methodArguments.toArray());

                } catch (Exception e) {
                    throw new CommandParseException("Failed to execute command method: " + methodInfo.getMethod().getName() + " ", e);
                }
            };

            bitsCommandManager.executeCommand(methodInfo.isAsync(), commandExecution);

            return Command.SINGLE_SUCCESS;
        };
    }


    private ArgumentBuilder<CommandSourceStack, ?> buildBackward(List<ArgumentBuilder<CommandSourceStack, ?>> toAdd) {
        if (toAdd.isEmpty()) throw new CommandParseException("No more branches to add.");
        if (toAdd.size() == 1) return toAdd.getFirst();

        ArgumentBuilder<CommandSourceStack, ?> first = toAdd.getFirst();
        List<ArgumentBuilder<CommandSourceStack, ?>> rest = toAdd.subList(1, toAdd.size());
        first.then(buildBackward(new ArrayList<>(rest)));
        return first;
    }

}
