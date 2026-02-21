/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.wrapper.collection;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

/**
 * Mutable container holding a single value.
 */
public final class Single<T> {

    /**
     * The wrapped value. May be {@code null}.
     */
    private @Nullable T value;

    /**
     * Creates a new {@code Single} containing the given value.
     */
    public Single(@Nullable T value) {
        this.value = value;
    }

    /**
     * Creates a new {@code Single} containing the given non-null value.
     */
    public static <T> Single<T> of(T value) {
        return new Single<>(Objects.requireNonNull(value, "value"));
    }

    /**
     * Creates a new {@code Single} with a {@code null} value.
     */
    public static <T> Single<T> empty() {
        return new Single<>(null);
    }

    /**
     * Returns the current value.
     */
    public @Nullable T get() {
        return value;
    }

    /**
     * Replaces the current value with the given value.
     */
    public void set(@Nullable T value) {
        this.value = value;
    }

    /**
     * Returns {@code true} if the current value is not {@code null}.
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Sets the current value to {@code null}.
     */
    public void clear() {
        this.value = null;
    }

    /**
     * Transforms the current value using the given mapper and returns
     * a new {@code Single} containing the mapped value.
     */
    public <R> Single<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        if (value == null) {
            return new Single<>(null);
        }
        return new Single<>(mapper.apply(value));
    }


    @Override
    public String toString() {
        return "Single[" + value + ']';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Single<?> other)) return false;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}
