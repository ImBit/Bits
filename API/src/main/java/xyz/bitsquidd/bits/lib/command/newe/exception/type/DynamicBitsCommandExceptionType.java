package xyz.bitsquidd.bits.lib.command.newe.exception.type;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newe.exception.BitsCommandException;

import java.util.function.Function;

public class DynamicBitsCommandExceptionType implements CommandExceptionType {
    private final Function<Object, String> function;

    public DynamicBitsCommandExceptionType(@NotNull Function<Object, String> function) {
        this.function = function;
    }

    public BitsCommandException create(Object arg) {
        return new BitsCommandException(this.function.apply(arg)) {};
    }

    public BitsCommandException createWithContext(ImmutableStringReader reader, Object arg) {
        return new BitsCommandException(this.function.apply(arg)) {};
    }
}