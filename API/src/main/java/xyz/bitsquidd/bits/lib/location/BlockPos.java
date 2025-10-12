package xyz.bitsquidd.bits.lib.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BlockPos {
    public final double x;
    public final double y;
    public final double z;

    public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);

    public BlockPos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static BlockPos of(double x, double y, double z) {
        return new BlockPos(x, y, z);
    }

    public static BlockPos of(Entity entity) {
        return new BlockPos(entity.getX(), entity.getY(), entity.getZ());
    }

    public static BlockPos of(Location location) {
        return new BlockPos(location.getX(), location.getY(), location.getZ());
    }


    public Location asLocation(World world) {
        return new Location(world, x, y, z);
    }

}
