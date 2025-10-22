package xyz.bitsquidd.bits.lib.command.argument.parser.impl.generic;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.command.argument.parser.impl.abs.AbstractEnumArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;
import java.util.stream.Stream;

/**
 * <b>Developer Note:</b> Custom enum parsers should override {@link AbstractEnumArgumentParser} if you want more fine-grained control over enum arguments / tab completion etc.
 * <p>
 * This implementation serves as a basic parser that should be fallen back upon when no specific enum parser is available.
 */
public final class GenericEnumParser<E extends Enum<E>> extends AbstractArgumentParserNew<@NotNull E> {

    private final Class<E> enumClass;

    public GenericEnumParser(Class<E> enumClass) {
        super(TypeSignature.of(enumClass), enumClass.getSimpleName());
        this.enumClass = enumClass;
    }

    @Override
    public @NotNull E parse(@NotNull List<Object> inputObjects, @NotNull BitsCommandContext ctx) {
        String inputString = singletonInputValidation(inputObjects, String.class);

        for (E constant : enumClass.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(inputString)) {
                return constant;
            }
        }

        throw new CommandParseException("Enum constant not found: " + inputString + " for enum " + enumClass.getSimpleName());
    }

    @Override
    public @NotNull List<String> getSuggestions(@NotNull BitsCommandContext ctx) {
        return Stream.of(enumClass.getEnumConstants()).map(Enum::name).toList();
    }
}
