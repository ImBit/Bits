package xyz.bitsquidd.bits.lib.command.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.Nullable;

public final class ArgumentPreconditions {
    private ArgumentPreconditions() {}

    public static void checkNotEmpty(@Nullable String string) throws CommandSyntaxException {
        if (string == null || string.isEmpty()) {
            throw ExceptionBuilder.createCommandException("Input string cannot be empty.");
        }
    }


    public static void checkNotNull(@Nullable Object obj) throws CommandSyntaxException {
        if (obj == null) {
            throw ExceptionBuilder.createCommandException("Input cannot be null.");
        }
    }

}
