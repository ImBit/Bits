package xyz.bitsquidd.bits.lib.command.parameters;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exception.ParamParseException;

import java.util.List;

public interface CommandParam<T> {
    T parse(CommandContext context, String arg) throws ParamParseException;
    List<String> tabComplete(CommandContext context, String current);
    String getTypeName();

    default int getExpectedArgCount() {
        return 1;
    }
}