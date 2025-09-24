package xyz.bitsquidd.bits.lib.command.newe.exception.type;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newe.exception.BitsCommandException;

public class DynamicBitsNCommandExceptionType implements CommandExceptionType {
    private final Function function;

    public DynamicBitsNCommandExceptionType(@NotNull Function function) {
        this.function = function;
    }

    public BitsCommandException create(Object... args) {
        return new BitsCommandException(this.function.apply(args)) {};
    }

    public BitsCommandException createWithContext(ImmutableStringReader reader, Object... args) {
        return new BitsCommandException(this.function.apply(args)) {};
    }

    public interface Function {
        String apply(Object[] args);
    }
}