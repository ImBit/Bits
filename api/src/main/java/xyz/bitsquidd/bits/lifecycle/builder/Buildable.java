/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lifecycle.builder;

/**
 * A functional interface for objects that can be built into a final instance.
 *
 * @param <B> the type of object that this builder produces
 *
 * @since 0.0.10
 */
public interface Buildable<B> {
    /**
     * Constructs and returns the final object instance.
     *
     * @return the built object
     *
     * @since 0.0.10
     */
    B build();

}
