package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class StringRemainderArgument implements CommandArgument<String> {
    public static final StringRemainderArgument INSTANCE = new StringRemainderArgument();
    
    @Override
    public String parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        if (startIndex >= context.getArgsLength()) {
            return "";
        }

        return String.join(" ", Arrays.copyOfRange(context.getArgs(), startIndex, context.getArgsLength()));
    }
    
    @Override
    public boolean canParseArg(@NotNull CommandContext context, int index) {
        return true;
    }
    
    @Override
    public boolean canParseFull(@NotNull CommandContext context, int startIndex) {
        return true;
    }

    @Override
    public @NotNull String getTypeName() {
        return "string...";
    }
    
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandContext context, int index) {
        return Collections.emptyList();
    }
}