package xyz.bitsquidd.bits.lib.wrappers;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Pair<A, B> {
    private A first;
    private B second;

    private Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Static factory method to create a new Pair instance.
     */
    public static <A, B> @NotNull Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    /**
     * Gets the first value of the pair.
     *
     * @return the value of the first element
     */
    public A getFirst() {
        return this.first;
    }

    /**
     * Gets the second value of the pair.
     *
     * @return the value of the second element
     */
    public B getSecond() {
        return this.second;
    }


    /**
     * Sets the first value of the pair.
     *
     * @param first the new value
     *
     * @return the old value (if present)
     */
    public A setFirst(A first) {
        A old = this.first;
        this.first = first;
        return old;
    }

    /**
     * Sets the second value of the pair.
     *
     * @param second the new value
     *
     * @return the old value (if present)
     */
    public B setSecond(B second) {
        B old = this.second;
        this.second = second;
        return old;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Pair<?, ?> pair) {
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
