/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.util.location.util;

/**
 * Represents the six cardinal directions in a 3D space.
 *
 * @since 0.0.11
 */
public enum Cardinal {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    UP,
    DOWN;

    public Cardinal opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST -> WEST;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case UP -> DOWN;
            case DOWN -> UP;
        };
    }

}
