package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.command.argument.parser.impl.abs.AbstractEnumArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;

/**
 * <b>Developer Note:</b> Enum parsers should override {@link AbstractEnumArgumentParser} for more specific enum operations i.e. tab completion.
 * <p>
 * This implementation serves as a basic parser that should be fallen back upon when no specific enum parser is available.
 */
public final class GenericEnumParser extends AbstractArgumentParserNew<@NotNull Enum<?>> {

    public GenericEnumParser() {
        super(TypeSignature.of(Enum.class), "Enum");
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Enum<?> parse(@NotNull List<Object> inputObjects, @NotNull BitsCommandContext ctx) {
        String inputString = singletonInputValidation(inputObjects, String.class);

        TypeSignature<?> typeSignature = getTypeSignature();
        Class<?> rawType = typeSignature.toRawType();
        if (!rawType.isEnum()) throw new CommandParseException("Type " + rawType.getName() + " is not an enum.");

        Class<Enum<?>> enumClass = (Class<Enum<?>>)rawType;

        for (Enum<?> constant : enumClass.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(inputString)) {
                return constant;
            }
        }

        throw new CommandParseException("Enum constant not found: " + inputString + " for enum " + enumClass.getName());
    }

}
