/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.util.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;

public interface Locatable {
    DecimalFormat df = new DecimalFormat("#.00");


    //region Convertors
    Location asLocation(World world);

    Block asBlock(World world);

    Vector asVector();
    //endregion


    //region Getters
    YawAndPitch getDirection();

    default double distance(Locatable locatable) {
        return Math.sqrt(distanceSquared(locatable));
    }

    default double distanceSquared(Locatable locatable) {
        Vector v1 = this.asVector();
        Vector v2 = locatable.asVector();

        double dx = v1.getX() - v2.getX();
        double dy = v1.getY() - v2.getY();
        double dz = v1.getZ() - v2.getZ();

        return dx * dx + dy * dy + dz * dz;
    }
    //endregion


    //region Math Functionality

    // TODO

    //endregion

}
