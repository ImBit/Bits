package xyz.bitsquidd.bits.lib.location;

import org.bukkit.Location;
import org.bukkit.World;

public class LocationHelper {

    public static boolean isWithinDistance(Location loc1, Location loc2, double distance) {
        if (loc1 == null || loc2 == null) {
            return false;
        }
        World world1 = loc1.getWorld();
        World world2 = loc2.getWorld();

        if (world1 == null || !world1.equals(world2)) {
            return false;
        }

        return loc1.distance(loc2) <= distance;
    }

    public static Location getMidpoint(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            return null;
        }
        World world1 = loc1.getWorld();
        World world2 = loc2.getWorld();

        if (world1 == null || !world1.equals(world2)) {
            return null;
        }

        double midX = (loc1.getX() + loc2.getX()) / 2;
        double midY = (loc1.getY() + loc2.getY()) / 2;
        double midZ = (loc1.getZ() + loc2.getZ()) / 2;

        return new Location(world1, midX, midY, midZ);
    }


    public static String toString(Location location) {
        if (location == null) {
            return "null";
        }
        World world = location.getWorld();
        String worldName = world != null ? world.getName() : "Unknown World";
        return String.format(
              "World: %s, X: %.2f, Y: %.2f, Z: %.2f, Pitch: %.2f, Yaw: %.2f",
              worldName, location.getX(), location.getY(), location.getZ(),
              location.getPitch(), location.getYaw()
        );
    }
}