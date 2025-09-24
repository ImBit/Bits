package xyz.bitsquidd.bits.lib.command.newe.exception;

import org.jetbrains.annotations.NotNull;

public class BCommandExecutionException extends AbstractCommandException {
    protected BCommandExecutionException(@NotNull String message) {
        super(message);
    }
}
