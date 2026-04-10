/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.location;

import org.bukkit.Location;
import org.bukkit.World;

import xyz.bitsquidd.bits.paper.location.wrapper.BlockPos;
import xyz.bitsquidd.bits.paper.location.wrapper.Locatable;

import java.util.Collection;

/**
 * Suite of location-related utility methods.
 *
 * @since 0.0.11
 */
public final class Locations {
    private Locations() {}

    /**
     * Safe distance check between two locations.
     * Returns false if either location is null or they are in different worlds.
     *
     * @since 0.0.11
     */
    public static boolean isWithinDistance(Location loc1, Location loc2, double distance) {
        if (loc1 == null || loc2 == null) return false;

        World world1 = loc1.getWorld();
        World world2 = loc2.getWorld();

        if (world1 == null || !world1.equals(world2)) return false;

        return loc1.distance(loc2) <= distance;
    }

    /**
     * Calculates the midpoint BlockPos coordinates from a collection of {@link Locatable}s.
     *
     * @since 0.0.13
     */
    public static BlockPos getMidpoint(Collection<? extends Locatable> locatables) {
        if (locatables.isEmpty()) throw new IllegalArgumentException("Locatables cannot be empty");

        int amount = locatables.size();
        double sumX = locatables.stream().mapToDouble(l -> l.asVector().getX()).sum();
        double sumY = locatables.stream().mapToDouble(l -> l.asVector().getY()).sum();
        double sumZ = locatables.stream().mapToDouble(l -> l.asVector().getZ()).sum();

        return BlockPos.of(sumX / amount, sumY / amount, sumZ / amount);
    }

    /**
     * Calculates the minimum BlockPos coordinates from a collection of {@link Locatable}s.
     *
     * @since 0.0.13
     */
    public static BlockPos getMinLocation(Collection<? extends Locatable> locatables) {
        if (locatables.isEmpty()) throw new IllegalArgumentException("Locatables cannot be empty");

        return BlockPos.of(
          locatables.stream().mapToDouble(l -> l.asVector().getX()).min().orElseThrow(() -> new IllegalArgumentException("Error computing min x")),
          locatables.stream().mapToDouble(l -> l.asVector().getY()).min().orElseThrow(() -> new IllegalArgumentException("Error computing min y")),
          locatables.stream().mapToDouble(l -> l.asVector().getZ()).min().orElseThrow(() -> new IllegalArgumentException("Error computing min z"))
        );
    }

    /**
     * Calculates the maximum BlockPos coordinates from a collection of {@link Locatable}s.
     *
     * @since 0.0.13
     */
    public static BlockPos getMaxLocation(Collection<? extends Locatable> locatables) {
        if (locatables.isEmpty()) throw new IllegalArgumentException("Locatables cannot be empty");

        return BlockPos.of(
          locatables.stream().mapToDouble(l -> l.asVector().getX()).max().orElseThrow(() -> new IllegalArgumentException("Error computing max x")),
          locatables.stream().mapToDouble(l -> l.asVector().getY()).max().orElseThrow(() -> new IllegalArgumentException("Error computing max y")),
          locatables.stream().mapToDouble(l -> l.asVector().getZ()).max().orElseThrow(() -> new IllegalArgumentException("Error computing max z"))
        );
    }

}