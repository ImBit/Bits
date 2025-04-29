package xyz.bitsquidd.bits.lib.command.params;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.List;

public interface CommandArgument<T> {
    String getTypeName();

    T parse(CommandContext context, int startIndex) throws ArgumentParseException;
    boolean canParseArg(CommandContext context, int argIndex);
    default int getRequiredArgs() {
        return 1;
    }
    default List<String> tabComplete(CommandContext context, int startIndex) {
        return List.of("");
    }

    default boolean canParseFull(CommandContext context, int startIndex) {
        try {
            parse(context, startIndex);
            return true;
        } catch (ArgumentParseException e) {
            return false;
        }
    }

}