package xyz.bitsquidd.bits.lib.command.exception;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public final class ExceptionBuilder {
    private ExceptionBuilder() {}

    public static CommandSyntaxException createCommandException(String stringMessage) {
        Message message = toMessage(stringMessage);
        return new CommandSyntaxException(createExceptionType(message), message);
    }

    private static SimpleCommandExceptionType createExceptionType(Message message) {
        return new SimpleCommandExceptionType(message);
    }

    private static Message toMessage(String message) {
        return () -> message;
    }

}
