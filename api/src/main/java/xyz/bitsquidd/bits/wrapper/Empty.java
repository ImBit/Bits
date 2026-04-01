/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.wrapper;

/**
 * A placeholder type used to represent an empty or absent value.
 *
 * @since 0.0.10
 */
public final class Empty {
    private Empty() {}

    /**
     * Returns a new empty instance.
     *
     * @return an empty instance
     *
     * @since 0.0.10
     */
    public static Empty empty() {
        return new Empty();
    }

}
