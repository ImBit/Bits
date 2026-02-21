/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lifecycle.builder;

public abstract class ExtendableBuildable<B, SELF extends ExtendableBuildable<B, SELF>> implements Buildable<B> {
    @SuppressWarnings("unchecked")
    protected final SELF self() {
        return (SELF)this;
    }

}
