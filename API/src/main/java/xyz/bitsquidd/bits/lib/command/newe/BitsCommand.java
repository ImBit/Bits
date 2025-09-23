package xyz.bitsquidd.bits.lib.command.newe;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BitsCommand {
    protected abstract @NotNull LiteralArgumentBuilder<CommandSourceStack> generateTree(final @NotNull LiteralArgumentBuilder<CommandSourceStack> root);

    public final @NotNull List<LiteralCommandNode<CommandSourceStack>> build(final @NotNull BitsCommandAnnotation annotation) {
        String rootName = annotation.name();
        if (rootName.isEmpty()) throw new IllegalStateException("Command class " + this.getClass().getName() + " has an empty name.");

        String[] aliases = annotation.aliases();
        List<String> commandNames = new ArrayList<>(List.of(rootName));
        commandNames.addAll(Arrays.asList(aliases));

        List<LiteralCommandNode<CommandSourceStack>> commandNodes = new ArrayList<>();

        commandNames.forEach(commandName -> {
            LiteralArgumentBuilder<CommandSourceStack> builder = generateTree(Commands.literal(commandName))
                  .requires(ctx -> Arrays.stream(annotation.permissions())
                        .allMatch(perm -> ctx.getSender().hasPermission(perm)));

            commandNodes.add(builder.build());
        });

        return commandNodes;
    }


    protected static @NotNull <T> T getArg(@NotNull CommandContext<CommandSourceStack> ctx, @NotNull String name, Class<T> clazz) {
        try {
            return ctx.getArgument(name, clazz);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Argument " + name + " is not of type " + clazz.getName());
        }
    }

    protected static @NotNull <T> T getArgOrDefault(@NotNull CommandContext<CommandSourceStack> ctx, @NotNull String name, Class<T> clazz, @NotNull T defaultValue) {
        try {
            return ctx.getArgument(name, clazz);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

}
