package xyz.bitsquidd.bits.lib.command.newe.exception;

import org.jetbrains.annotations.NotNull;

public class CommandExecutionException extends AbstractCommandException {
    protected CommandExecutionException(@NotNull String message) {
        super(message);
    }
}
