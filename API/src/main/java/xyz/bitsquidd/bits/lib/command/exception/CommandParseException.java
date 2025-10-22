package xyz.bitsquidd.bits.lib.command.exception;

public class CommandParseException extends IllegalArgumentException {
    public CommandParseException(String message) {
        super(message);
    }

    public CommandParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
