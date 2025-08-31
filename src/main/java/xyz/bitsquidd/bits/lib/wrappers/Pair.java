package xyz.bitsquidd.bits.lib.wrappers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Pair<A, B> {
    private @Nullable A first;
    private @Nullable B second;

    public Pair(@Nullable A first, @Nullable B second) {
        this.first = first;
        this.second = second;
    }

    public static <A, B> @NotNull Pair<A, B> of(@Nullable A first, @Nullable B second) {
        return new Pair<>(first, second);
    }


    public @Nullable A getFirst() {
        return this.first;
    }

    public @Nullable B getSecond() {
        return this.second;
    }

    public void setFirst(@Nullable A first) {
        this.first = first;
    }

    public void setSecond(@Nullable B second) {
        this.second = second;
    }


    @SuppressWarnings("RawUseOfParameterized")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Pair<?, ?> pair = (Pair)o;
            return Objects.equals(this.first, pair.first) && Objects.equals(this.second, pair.second);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }

    @Override
    public String toString() {
        return "Pair{" +
              "first=" + first +
              ", second=" + second +
              '}';
    }
}
