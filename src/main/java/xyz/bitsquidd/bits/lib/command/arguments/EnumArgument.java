package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.arguments.interfaces.CommandArgument;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumArgument<T extends Enum<T>> extends CommandArgument<T> {
    private final Class<T> enumClass;
    private final Set<T> allowedValues;

    private EnumArgument(Class<T> enumClass, Set<T> allowedValues) {
        this.enumClass = enumClass;
        this.allowedValues = allowedValues;
    }

    public static <T extends Enum<T>> EnumArgument<T> of(Class<T> enumClass) {
        Set<T> values = Arrays.stream(enumClass.getEnumConstants()).collect(Collectors.toSet());
        return new EnumArgument<>(enumClass, values);
    }

    public static <T extends Enum<T>> EnumArgument<T> of(Class<T> enumClass, T... allowedValues) {
        Set<T> values = Arrays.stream(allowedValues).collect(Collectors.toSet());
        return new EnumArgument<>(enumClass, values);
    }

    @Override
    public String getTypeName() {
        return enumClass.getSimpleName();
    }

    @Override
    public T parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        String input = context.getArg(startIndex).toUpperCase();

        try {
            T value = Enum.valueOf(enumClass, input);
            if (allowedValues.isEmpty() || allowedValues.contains(value)) {
                return value;
            }
            throw new ArgumentParseException("Invalid " + enumClass.getSimpleName() + ". Valid values: " +
                    getAllowedValuesString());
        } catch (IllegalArgumentException e) {
            throw new ArgumentParseException("Invalid " + enumClass.getSimpleName() + ". Valid values: " +
                    getAllowedValuesString());
        }
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int index) {
        try {
            String input = context.getArg(index).toUpperCase();
            T value = Enum.valueOf(enumClass, input);
            return allowedValues.isEmpty() || allowedValues.contains(value);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandContext context, int index) {
        if (allowedValues.isEmpty()) {
            return Arrays.stream(enumClass.getEnumConstants())
                         .map(Enum::name)
                         .collect(Collectors.toList());
        } else {
            return allowedValues.stream()
                                .map(Enum::name)
                                .collect(Collectors.toList());
        }
    }

    private String getAllowedValuesString() {
        return allowedValues.stream()
                            .map(Enum::name)
                            .collect(Collectors.joining(", "));
    }
}