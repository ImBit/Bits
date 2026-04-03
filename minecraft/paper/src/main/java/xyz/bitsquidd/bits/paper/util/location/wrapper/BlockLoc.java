/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.util.location.wrapper;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Objects;

/**
 * A basic implementation of an integer position in a 3D space.
 *
 * @param x The x-coordinate of the block position.
 * @param y The y-coordinate of the block position.
 * @param z The z-coordinate of the block position.
 *
 * @since 0.0.11
 */
public record BlockLoc(
  int x,
  int y,
  int z
) implements Locatable {

    //region Java Methods
    @Override
    public String toString() {
        return x + ", " + y + ", " + z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BlockLoc(int x1, int y1, int z1))) return false;

        return this.x == x1
          && this.y == y1
          && this.z == z1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
    //endregion


    //region Convertors
    @Override
    public Location asLocation(World world) {
        return new Location(world, x + 0.5, y + 0.5, z + 0.5);
    }

    @Override
    public Block asBlock(World world) {
        return world.getBlockAt(x, y, z);
    }

    @Override
    public Vector asVector() {
        return new Vector(x, y, z);
    }
    //endregion


    //region Getters

    @Override
    public YawAndPitch getDirection() {
        return YawAndPitch.ZERO;
    }


    //endregion

}
