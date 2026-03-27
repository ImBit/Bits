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
) {

    public BlockCardinal add(BlockCardinal other) {
        return new BlockCardinal(
          this.x + other.x,
          this.y + other.y,
          this.z + other.z,
          YawAndPitch.from(cardinal).addWrap(YawAndPitch.from(other.cardinal)).toCardinal()
        );
    }

    public Location toLocation(World world) {
        YawAndPitch yawAndPitch = YawAndPitch.from(cardinal);
        return new Location(world, x + 0.5, y + 0.5, z + 0.5, yawAndPitch.yaw, yawAndPitch.pitch);
    }

}
