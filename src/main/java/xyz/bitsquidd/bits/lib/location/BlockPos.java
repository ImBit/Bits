package xyz.bitsquidd.bits.lib.location;

import org.bukkit.Location;
import org.bukkit.World;

public class BlockPos {
    public final float x;
    public final float y;
    public final float z;

    public BlockPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public static BlockPos of(float x, float y, float z) {
        return new BlockPos(x, y, z);
    }


    public Location asLocation(World world) {
        return new Location(world, x, y, z);
    }
}
