package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.arguments.interfaces.CommandArgument;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.List;

public class SimpleOptionArgument extends CommandArgument<String> {
    private final String expectedValue;

    private SimpleOptionArgument(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public static SimpleOptionArgument of(String expectedValue) {
        return new SimpleOptionArgument(expectedValue);
    }

    @Override
    public String parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        String input = context.getArg(startIndex);
        if (input.equalsIgnoreCase(expectedValue)) {
            return expectedValue;
        }
        throw new ArgumentParseException("Expected '" + expectedValue + "'");
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int index) {
        String input = context.getArg(index);
        return input.equalsIgnoreCase(expectedValue);
    }

    @Override
    public @NotNull String getTypeName() {
        return "";
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandContext context, int index) {
        return List.of(expectedValue);
    }
}