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
 * Immutable representation of a Bukkit Location.
 */
public record ImmutableLocation(
  World world,
  double x,
  double y,
  double z,
  float yaw,
  float pitch
) {
    public static ImmutableLocation of(Location loc) {
        return new ImmutableLocation(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public Location toBukkit() {
        return new Location(world, x, y, z, yaw, pitch);
    }

}