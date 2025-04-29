package xyz.bitsquidd.bits.lib.command.params;

import xyz.bitsquidd.bits.lib.command.CommandContextNew;
import xyz.bitsquidd.bits.lib.command.exceptions.ParamParseException;

import java.util.List;

public interface CommandParamNew<T> {
    String getTypeName();

    T parse(CommandContextNew context, int startIndex) throws ParamParseException;
    boolean canParseArg(CommandContextNew context, int argIndex);
    default int getRequiredArgs() {
        return 1;
    }
    default List<String> tabComplete(CommandContextNew context) {
        return List.of("HI"+context.getArgLength());
    }

    default boolean canParseFull(CommandContextNew context, int startIndex) {
        try {
            parse(context, startIndex);
            return true;
        } catch (ParamParseException e) {
            return false;
        }
    }

}