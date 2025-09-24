package xyz.bitsquidd.bits.lib.command.newe.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.command.newe.exception.AbstractCommandException;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.CommandReturnDecorator;

import java.util.concurrent.CompletableFuture;

@NullMarked
public abstract class BitsCommandArgument<T, N> implements CustomArgumentType<T, N> {
    protected static final SimpleCommandExceptionType ERROR_BAD_SOURCE = new SimpleCommandExceptionType(
          MessageComponentSerializer.message().serialize(Component.text("The source needs to be a CommandSourceStack!"))
    );

    protected static final DynamicCommandExceptionType ERROR_EMPTY = new DynamicCommandExceptionType(
          input -> MessageComponentSerializer.message().serialize(Component.text("No argument specified"))
    );


    @Override
    public final T parse(StringReader reader) throws CommandSyntaxException {
        throw new UnsupportedOperationException("This method will never be called.");
    }

    @Override
    public final <S> T parse(StringReader reader, S source) throws CommandSyntaxException {
        if (!(source instanceof CommandSourceStack stack)) throw ERROR_BAD_SOURCE.create();
        
        try {
            return parseSafe(reader, stack);
        } catch (AbstractCommandException e) {
            Text.of(e.componentMessage())
                  .decorate(CommandReturnDecorator.of(CommandReturnType.ERROR))
                  .send(stack.getSender());
        }
    }

    protected abstract T parseSafe(StringReader reader, CommandSourceStack stack) throws AbstractCommandException;

    @Override
    public final <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
        if (!(ctx.getSource() instanceof CommandSourceStack)) return builder.buildFuture();

        return listSuggestionsSafe(ctx, builder);
    }

    protected abstract <S> CompletableFuture<Suggestions> listSuggestionsSafe(CommandContext<S> ctx, SuggestionsBuilder builder);
}
