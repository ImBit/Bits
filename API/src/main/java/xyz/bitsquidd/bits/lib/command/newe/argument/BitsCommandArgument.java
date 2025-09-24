package xyz.bitsquidd.bits.lib.command.newe.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public abstract class BitsCommandArgument<T, N> implements CustomArgumentType<T, N> {
    private static final SimpleCommandExceptionType ERROR_BAD_SOURCE = new SimpleCommandExceptionType(
          MessageComponentSerializer.message().serialize(Component.text("The source needs to be a CommandSourceStack!"))
    );

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        throw new UnsupportedOperationException("This method will never be called.");
    }

    @Override
    public <S> T parse(StringReader reader, S source) throws CommandSyntaxException {
        if (!(source instanceof CommandSourceStack stack)) throw ERROR_BAD_SOURCE.create();

        return parseSafe(reader, stack);
    }

    abstract T parseSafe(StringReader reader, CommandSourceStack stack);

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
        if (!(ctx.getSource() instanceof CommandSourceStack)) return builder.buildFuture();

        return listSuggestionsSafe(ctx, builder);
    }

    abstract <S> CompletableFuture<Suggestions> listSuggestionsSafe(CommandContext<S> ctx, SuggestionsBuilder builder);
}
