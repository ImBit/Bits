package xyz.bitsquidd.bits.lib.command.newe.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.helper.EnumHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@NullMarked
public abstract class AbstractEnumArgument<T extends Enum<T>> extends BitsCommandArgument<T, String> {

    private final Class<T> enumClass;
    private final Predicate<T> allowedValues;

    private final DynamicCommandExceptionType ERROR_NOT_VALID;
    private final DynamicCommandExceptionType ERROR_NOT_ALLOWED;

    public AbstractEnumArgument(Class<T> enumClass, Predicate<T> allowedValues) {
        this.enumClass = enumClass;
        this.allowedValues = allowedValues;

        ERROR_NOT_VALID = new DynamicCommandExceptionType(
              name -> MessageComponentSerializer.message().serialize(Component.text(name + " is not a valid " + enumClass.getSimpleName()))
        );

        ERROR_NOT_ALLOWED = new DynamicCommandExceptionType(
              name -> MessageComponentSerializer.message().serialize(Component.text(name + " is not allowed to be used in this command!"))
        );
    }

    @Override
    protected T parseSafe(StringReader stringReader, CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        final String name = getNativeType().parse(stringReader);
        T enumValue = EnumHelper.valueOf(this.enumClass, name, null);

        if (enumValue == null) throw ERROR_NOT_VALID.create(name);
        if (!allowedValues.test(enumValue)) throw ERROR_NOT_ALLOWED.create(name);

        return enumValue;
    }

    @Override
    protected <S> CompletableFuture<Suggestions> listSuggestionsSafe(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
        for (T value : enumClass.getEnumConstants()) {
            if (allowedValues.test(value)) {
                suggestionsBuilder.suggest(value.name().toUpperCase());
            }
        }

        return suggestionsBuilder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
