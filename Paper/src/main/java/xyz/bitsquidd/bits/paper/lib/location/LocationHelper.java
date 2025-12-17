package xyz.bitsquidd.bits.paper.lib.location;

import org.bukkit.Location;
import org.bukkit.World;

public class LocationHelper {

    /**
     * Safe distance check between two locations.
     * Returns false if either location is null or they are in different worlds.
     *
     * @param loc1     The first location to compare.
     * @param loc2     The second location to compare.
     * @param distance The distance threshold.
     *
     * @return true if the locations are within the specified distance, false otherwise.
     */
    public static boolean isWithinDistance(Location loc1, Location loc2, double distance) {
        if (loc1 == null || loc2 == null) return false;

        World world1 = loc1.getWorld();
        World world2 = loc2.getWorld();

        if (world1 == null || !world1.equals(world2)) return false;

        return loc1.distance(loc2) <= distance;
    }

    /**
     * Calculates the midpoint between two {@link Location}s.
     *
     * @param loc1 The first location.
     * @param loc2 The second location.
     *
     * @return A new Location representing the midpoint between the two locations.
     */
    public static Location getMidpoint(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) throw new IllegalArgumentException("Locations cannot be null");

        World world1 = loc1.getWorld();
        World world2 = loc2.getWorld();

        if (world1 == null || !world1.equals(world2)) throw new IllegalArgumentException("Locations must be in the same world");

        double midX = (loc1.getX() + loc2.getX()) / 2;
        double midY = (loc1.getY() + loc2.getY()) / 2;
        double midZ = (loc1.getZ() + loc2.getZ()) / 2;

        return new Location(world1, midX, midY, midZ);
    }

}