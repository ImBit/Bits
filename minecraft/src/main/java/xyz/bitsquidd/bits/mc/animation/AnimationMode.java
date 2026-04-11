/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.animation;

/**
 * Defines how an animation should play.
 *
 * @since 0.0.13
 */
public enum AnimationMode {
    STRAIGHT,   // Loops back to the start
    PING_PONG   // Reverses direction each cycle
}