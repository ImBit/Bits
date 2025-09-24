package xyz.bitsquidd.bits.lib.command.newe.exception;

import org.jetbrains.annotations.NotNull;

public class CommandParseException extends AbstractCommandException {
    protected CommandParseException(@NotNull String message) {
        super(message);
    }
}
