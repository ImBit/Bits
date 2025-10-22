package xyz.bitsquidd.bits.lib.command.argument.type;

import org.jetbrains.annotations.NotNull;

public class GreedyString {
    public final @NotNull String value;

    public GreedyString(@NotNull String value) {
        this.value = value;
    }

    public static @NotNull GreedyString of(@NotNull String value) {
        return new GreedyString(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof GreedyString other) && other.value.equals(this.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
