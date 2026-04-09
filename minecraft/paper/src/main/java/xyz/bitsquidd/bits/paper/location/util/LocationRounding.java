/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.location.util;

import org.bukkit.Location;

public enum LocationRounding {
    NONE,          // Nothing applied
    CENTRE_0Y,     // Centre of the block, with 0y.
    CENTRE,        // Centre of the block
    PIXEL,         // Rounded to the nearest pixel (1/16th of a block).
    ;

    public final Location applyTo(Location location) {
        return switch (this) {
            case CENTRE_0Y -> {
                location.setX(location.getBlockX() + 0.5f);
                location.setY(location.getBlockY() + 0.0f);
                location.setZ(location.getBlockZ() + 0.5f);
                yield location;
            }
            case CENTRE -> {
                location.setX(location.getBlockX() + 0.5f);
                location.setY(location.getBlockY() + 0.5f);
                location.setZ(location.getBlockZ() + 0.5f);
                yield location;
            }
            case PIXEL -> {
                location.setX(Math.round(location.getX() * 16) / 16.0);
                location.setY(Math.round(location.getY() * 16) / 16.0);
                location.setZ(Math.round(location.getZ() * 16) / 16.0);
                yield location;
            }
            default -> location;
        };
    }
}
