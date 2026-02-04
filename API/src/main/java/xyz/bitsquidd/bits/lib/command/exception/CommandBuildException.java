package xyz.bitsquidd.bits.lib.command.exception;

public class CommandBuildException extends RuntimeException {
    public CommandBuildException(String message) {
        super(message);
    }

    public CommandBuildException(String message, Throwable cause) {
        super(message, cause);
    }

}
