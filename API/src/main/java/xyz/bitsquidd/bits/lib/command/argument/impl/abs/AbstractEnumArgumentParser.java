package xyz.bitsquidd.bits.lib.command.argument.impl.abs;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;
import java.util.stream.Stream;

@NullMarked
public class AbstractEnumArgumentParser<T extends Enum<T>> extends AbstractArgumentParserNew<T> {
    private final Class<T> enumClass;

    public AbstractEnumArgumentParser(Class<T> enumClass) {
        super(TypeSignature.of(Enum.class), enumClass.getName());
        this.enumClass = enumClass;
        if (!enumClass.isEnum()) throw new IllegalArgumentException("Provided class " + enumClass.getName() + " is not an enum!");
    }

    @Override
    public @NotNull T parse(List<Object> inputObjects, BitsCommandContext ctx) throws CommandParseException {
        String inputString = singletonInputValidation(inputObjects, String.class);

        T enumValue;
        try {
            enumValue = Enum.valueOf(enumClass, inputString);
        } catch (IllegalArgumentException e) {
            throw new CommandParseException(inputString + " is not a valid " + enumClass.getSimpleName() + ".");
        }

        return enumValue;
    }

    @Override
    public List<TypeSignature<?>> getInputTypes() {
        return List.of(TypeSignature.of(String.class));
    }

    @Override
    public List<String> getSuggestions(BitsCommandContext ctx) {
        return enumClass.isEnum() ? Stream.of(enumClass.getEnumConstants()).map(Enum::name).toList() : List.of();
    }

}