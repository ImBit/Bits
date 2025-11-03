package xyz.bitsquidd.bits.lib.type;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class GreedyString {
    public final String value;

    public GreedyString(String value) {
        this.value = value;
    }

    public static GreedyString of(String value) {
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
