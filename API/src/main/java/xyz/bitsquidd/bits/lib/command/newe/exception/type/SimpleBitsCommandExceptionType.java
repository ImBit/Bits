package xyz.bitsquidd.bits.lib.command.newe.exception.type;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newe.exception.BitsCommandException;

public class SimpleBitsCommandExceptionType implements CommandExceptionType {
    private final String message;

    public SimpleBitsCommandExceptionType(@NotNull String message) {
        this.message = message;
    }

    public BitsCommandException create() {
        return new BitsCommandException(this.message) {};
    }

    public BitsCommandException createWithContext(ImmutableStringReader reader) {
        return new BitsCommandException(this.message) {};
    }

    @Override
    public String toString() {
        return this.message;
    }
}