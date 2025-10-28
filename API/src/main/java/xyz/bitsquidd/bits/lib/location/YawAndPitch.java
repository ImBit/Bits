package xyz.bitsquidd.bits.lib.location;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.joml.Quaternionf;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class YawAndPitch {
    public final float yaw;
    public final float pitch;

    public static final YawAndPitch ZERO = new YawAndPitch(0f, 0f);

    private YawAndPitch(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static YawAndPitch of(float yaw, float pitch) {
        return new YawAndPitch(pitch, yaw);
    }


    public static YawAndPitch from(Location location) {
        return new YawAndPitch(location.getYaw(), location.getPitch());
    }

    public static YawAndPitch from(BlockFace blockFace) {
        return switch (blockFace) {
            case NORTH -> new YawAndPitch(0, 0);
            case EAST -> new YawAndPitch(90, 0);
            case SOUTH -> new YawAndPitch(180, 0);
            case WEST -> new YawAndPitch(-90, 0);
            case UP -> new YawAndPitch(0, -90);
            case DOWN -> new YawAndPitch(0, 90);
            case NORTH_EAST -> new YawAndPitch(45, 0);
            case SOUTH_EAST -> new YawAndPitch(135, 0);
            case SOUTH_WEST -> new YawAndPitch(-135, 0);
            case NORTH_WEST -> new YawAndPitch(-45, 0);
            case WEST_NORTH_WEST -> new YawAndPitch(-67.5f, 0);
            case NORTH_NORTH_WEST -> new YawAndPitch(-22.5f, 0);
            case NORTH_NORTH_EAST -> new YawAndPitch(22.5f, 0);
            case EAST_NORTH_EAST -> new YawAndPitch(67.5f, 0);
            case EAST_SOUTH_EAST -> new YawAndPitch(112.5f, 0);
            case SOUTH_SOUTH_EAST -> new YawAndPitch(157.5f, 0);
            case SOUTH_SOUTH_WEST -> new YawAndPitch(-157.5f, 0);
            case WEST_SOUTH_WEST -> new YawAndPitch(-112.5f, 0);

            default -> new YawAndPitch(0, 0);
        };
    }

    public void applyTo(Location location) {
        location.setPitch(pitch);
        location.setYaw(yaw);
    }

    public Quaternionf toQuaternion() {
        Quaternionf quaternion = new Quaternionf();
        quaternion.rotateY((float)Math.toRadians(yaw));
        quaternion.rotateX((float)Math.toRadians(pitch));
        return quaternion;
    }

    public Quaternionf addToQuaternion(Quaternionf quaternion) {
        quaternion.rotateY((float)Math.toRadians(yaw));
        quaternion.rotateX((float)Math.toRadians(pitch));
        return quaternion;
    }

}
