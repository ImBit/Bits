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

import java.util.Objects;

/**
 * Represents an integer block position with an associated cardinal direction.
 *
 * @param x        The x-coordinate of the block position.
 * @param y        The y-coordinate of the block position.
 * @param z        The z-coordinate of the block position.
 * @param cardinal The cardinal direction associated with this block position.
 *
 * @since 0.0.11
 */
public record BlockCardinal(
  int x,
  int y,
  int z,
  Cardinal cardinal
) implements Locatable {

    //region Java Methods
    @Override
    public String toString() {
        return x + ", " + y + ", " + z + ", " + cardinal;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BlockCardinal(int x1, int y1, int z1, Cardinal cardinal1))) return false;

        return this.x == x1
          && this.y == y1
          && this.z == z1
          && this.cardinal == cardinal1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, cardinal);
    }
    //endregion


    //region Convertors
    @Override
    public Location asLocation(World world) {
        YawAndPitch yawAndPitch = YawAndPitch.from(cardinal);
        return new Location(world, x + 0.5, y + 0.5, z + 0.5, yawAndPitch.yaw, yawAndPitch.pitch);
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
        return YawAndPitch.from(cardinal);
    }


    //endregion


    public BlockCardinal add(int x, int y, int z) {
        return new BlockCardinal(this.x + x, this.y + y, this.z + z, cardinal);
    }

    public BlockCardinal add(BlockCardinal other) {
        return new BlockCardinal(
          this.x + other.x,
          this.y + other.y,
          this.z + other.z,
          YawAndPitch.from(cardinal).addWrap(YawAndPitch.from(other.cardinal)).toCardinal()
        );
    }

}
