package xyz.bitsquidd.bits.lib.command.argument.parser.impl.abs;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@NullMarked
public abstract class AbstractEnumArgumentParser<T extends Enum<T>> extends AbstractArgumentParser<T> {
    private final Class<T> enumClass;

    public AbstractEnumArgumentParser(Class<T> enumClass) {
        super(TypeSignature.of(Enum.class), enumClass.getName());
        this.enumClass = enumClass;
        if (!enumClass.isEnum()) throw new IllegalArgumentException("Provided class " + enumClass.getName() + " is not an enum!");
    }

    @Override
    public T parse(List<Object> inputObjects, BitsCommandContext ctx) throws CommandParseException {
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
    public @Nullable Supplier<List<String>> getSuggestions() {
        return () -> enumClass.isEnum() ? Stream.of(enumClass.getEnumConstants()).map(Enum::name).toList() : List.of();
    }

}