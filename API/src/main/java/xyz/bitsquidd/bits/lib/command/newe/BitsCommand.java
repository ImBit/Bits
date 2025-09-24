package xyz.bitsquidd.bits.lib.command.newe;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.resolvers.ArgumentResolver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BitsCommand {
    protected abstract @NotNull LiteralArgumentBuilder<CommandSourceStack> generateTree(final @NotNull LiteralArgumentBuilder<CommandSourceStack> root);

    public final @NotNull List<LiteralCommandNode<CommandSourceStack>> build(final @NotNull BitsCommandAnnotation annotation, final @NotNull List<String> addedPermissions) {
        String rootName = annotation.name();
        if (rootName.isEmpty()) throw new IllegalStateException("Command class " + this.getClass().getName() + " has an empty name.");

        String[] aliases = annotation.aliases();
        List<String> commandNames = new ArrayList<>(List.of(rootName));
        commandNames.addAll(Arrays.asList(aliases));

        List<LiteralCommandNode<CommandSourceStack>> commandNodes = new ArrayList<>();

        List<String> allPermissions = new ArrayList<>(List.of(annotation.permissions()));
        allPermissions.addAll(addedPermissions);

        commandNames.forEach(commandName -> {
            LiteralArgumentBuilder<CommandSourceStack> builder = generateTree(Commands.literal(commandName))
                  .requires(ctx -> allPermissions.stream()
                        .allMatch(perm -> ctx.getSender().hasPermission(perm)));

            commandNodes.add(builder.build());
        });

        return commandNodes;
    }

    /**
     * Helper method to get an argument from the command context.
     *
     * @param ctx   The command context
     * @param name  The name of the argument
     * @param clazz The class of the argument
     * @param <T>   The type of the argument
     *
     * @return The argument
     */
    protected static <T> T getArg(
          @NotNull CommandContext<CommandSourceStack> ctx,
          @NotNull String name,
          @NotNull Class<T> clazz
    ) {
        return ctx.getArgument(name, clazz);
    }

    /**
     * Helper method to get an argument from the command context, or a default value if the argument is not present.
     *
     * @param ctx          The command context
     * @param name         The name of the argument
     * @param clazz        The class of the argument
     * @param defaultValue The default value to return if the argument is not present
     * @param <T>          The type of the argument
     *
     * @return The argument, or the default value
     */
    protected static <T> T getArgOrDefault(
          @NotNull CommandContext<CommandSourceStack> ctx,
          @NotNull String name,
          @NotNull Class<T> clazz,
          @NotNull T defaultValue
    ) {
        try {
            return getArg(ctx, name, clazz);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Helper method to resolve an argument using an ArgumentResolver.
     *
     * @param ctx           The command context
     * @param name          The name of the argument
     * @param resolverClass The class of the ArgumentResolver
     * @param <T>           The type of the resolved argument
     * @param <R>           The type of the ArgumentResolver
     *
     * @return The resolved argument
     */
    protected static <T, R extends ArgumentResolver<T>> T resolveArg(
          @NotNull CommandContext<CommandSourceStack> ctx,
          @NotNull String name,
          @NotNull Class<R> resolverClass
    ) throws CommandSyntaxException {
        R resolver = ctx.getArgument(name, resolverClass);
        return resolver.resolve(ctx.getSource());
    }

    /**
     * Helper method to resolve an argument using an ArgumentResolver, or a default value if the resolution fails.
     *
     * @param ctx           The command context
     * @param name          The name of the argument
     * @param resolverClass The class of the ArgumentResolver
     * @param defaultValue  The default value to return if the resolution fails
     * @param <T>           The type of the resolved argument
     * @param <R>           The type of the ArgumentResolver
     *
     * @return The resolved argument, or the default value
     */
    protected static <T, R extends ArgumentResolver<T>> T resolveArgOrDefault(
          @NotNull CommandContext<CommandSourceStack> ctx,
          @NotNull String name,
          @NotNull Class<R> resolverClass,
          @NotNull T defaultValue
    ) {
        try {
            return resolveArg(ctx, name, resolverClass);
        } catch (Exception e) {
            return defaultValue;
        }
    }


}
