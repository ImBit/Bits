package xyz.bitsquidd.bits.lib.command.newe.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.newe.exception.BCommandParseException;

import java.util.concurrent.CompletableFuture;

@NullMarked
public abstract class BitsCommandArgument<T, N> implements CustomArgumentType<T, N> {
    protected static final BCommandParseException ERROR_BAD_SOURCE = new BCommandParseException("The source needs to be a CommandSourceStack!");


    @Override
    public final T parse(StringReader reader) throws CommandSyntaxException {
        throw new UnsupportedOperationException("This method will never be called.");
    }

    @Override
    public final <S> T parse(StringReader reader, S source) throws CommandSyntaxException {
        if (!(source instanceof CommandSourceStack stack)) throw ERROR_BAD_SOURCE;

        return parseSafe(reader, stack);
    }

    protected abstract T parseSafe(StringReader reader, CommandSourceStack stack) throws CommandSyntaxException;

    @Override
    public final <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
        if (!(ctx.getSource() instanceof CommandSourceStack)) return builder.buildFuture();

        return listSuggestionsSafe(ctx, builder);
    }

    protected abstract <S> CompletableFuture<Suggestions> listSuggestionsSafe(CommandContext<S> ctx, SuggestionsBuilder builder);
}
