/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.wrapper;

/**
 * A wrapper class for holding a string. Usually used for command parsing of strings with spaces.
 */
public final class GreedyString {
    public final String value;

    private GreedyString(String value) {
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
