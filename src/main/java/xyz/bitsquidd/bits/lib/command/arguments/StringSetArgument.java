package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.arguments.interfaces.CommandArgument;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StringSetArgument extends CommandArgument<String> {
    private final Set<String> allowedValues;
    private final String typeName;
    private final boolean caseSensitive;

    private StringSetArgument(Set<String> allowedValues, String typeName, boolean caseSensitive) {
        this.allowedValues = caseSensitive ? 
                allowedValues : 
                allowedValues.stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());
        this.typeName = typeName;
        this.caseSensitive = caseSensitive;
    }

    public static StringSetArgument of(String typeName, String... allowedValues) {
        return new StringSetArgument(
                Arrays.stream(allowedValues).collect(Collectors.toSet()),
                typeName,
                false
        );
    }

    public static StringSetArgument of(String typeName, boolean caseSensitive, String... allowedValues) {
        return new StringSetArgument(
                Arrays.stream(allowedValues).collect(Collectors.toSet()),
                typeName,
                caseSensitive
        );
    }

    @Override
    public String parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        String input = context.getArg(startIndex);
        String compareInput = caseSensitive ? input : input.toLowerCase();
        
        if (allowedValues.contains(compareInput)) {
            return input;
        }
        
        throw new ArgumentParseException("Invalid " + typeName + ". Valid values: " +
                getAllowedValuesString());
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int index) {
        String input = context.getArg(index);
        String compareInput = caseSensitive ? input : input.toLowerCase();
        return allowedValues.contains(compareInput);
    }

    @Override
    public @NotNull String getTypeName() {
        return typeName;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandContext context, int index) {
        return allowedValues.stream().toList();
    }

    private String getAllowedValuesString() {
        return String.join(", ", allowedValues);
    }
}