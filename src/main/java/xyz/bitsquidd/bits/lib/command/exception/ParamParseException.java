package xyz.bitsquidd.bits.lib.command.exception;

public class ParamParseException extends Exception {
    public ParamParseException(String message) {
        super(message);
    }
    public ParamParseException(String message, Throwable cause) {
        super(message, cause);
    }
    public String getErrorMessage() {
        return getMessage();
    }
}