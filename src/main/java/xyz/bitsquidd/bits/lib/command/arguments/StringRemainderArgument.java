package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.arguments.interfaces.CommandArgument;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class StringRemainderArgument extends CommandArgument<String> {
    public static final StringRemainderArgument INSTANCE = new StringRemainderArgument();

    @Override
    public String parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        if (startIndex >= context.getArgsLength()) {
            throw new ArgumentParseException("No string argument provided");
        }

        String result = String.join(" ", Arrays.copyOfRange(context.getArgs(), startIndex, context.getArgsLength()));

        if (result.isEmpty()) {
            throw new ArgumentParseException("Empty string is not allowed");
        }

        return result;
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int index) {
        return index < context.getArgsLength() && !context.getArg(index).isEmpty();
    }

    @Override
    public boolean canParseFull(@NotNull CommandContext context, int startIndex) {
        if (startIndex >= context.getArgsLength()) {
            return false;
        }

        String result = String.join(" ", Arrays.copyOfRange(context.getArgs(), startIndex, context.getArgsLength()));
        return !result.isEmpty();
    }

    @Override
    public @NotNull String getTypeName() {
        return "String...";
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandContext context, int index) {
        return Collections.emptyList();
    }

    @Override
    public boolean isGreedy() {
        return true;
    }
}