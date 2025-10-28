package xyz.bitsquidd.bits.lib.location;

import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Immutable class representing a position in the world with x, y, z coordinates and yaw, pitch rotation.
 */
@NullMarked
public final class BlockPos {
    public final double x;
    public final double y;
    public final double z;

    public final float yaw;
    public final float pitch;

    public static final BlockPos ORIGIN = new BlockPos(0, 0, 0, 0, 0);

    private static final DecimalFormat df = new DecimalFormat("#.00");

    private BlockPos(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = (yaw + 180) % 360 - 180;   // Normalizes yaw to   [-180, 180)
        this.pitch = (pitch + 90) % 180 - 90; // Normalizes pitch to [-90, 90]
    }

    public static BlockPos of(double x, double y, double z) {
        return new BlockPos(x, y, z, 0, 0);
    }

    public static BlockPos of(double x, double y, double z, float yaw, float pitch) {
        return new BlockPos(x, y, z, yaw, pitch);
    }

    public static BlockPos of(Entity entity) {
        return new BlockPos(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
    }

    public static BlockPos of(Location location) {
        return new BlockPos(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }


    @Override
    public String toString() {
        return df.format(x) + "," + df.format(y) + "," + df.format(z) + "," + df.format(yaw) + "," + df.format(pitch);
    }

    public static BlockPos fromString(String str) {
        String trimmed = str.trim();
        String[] parts = trimmed.split(",");
        if (parts.length != 5 && parts.length != 3) throw new IllegalArgumentException("Invalid BlockPos string: " + str);
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);
        if (parts.length == 5) {
            float yaw = Float.parseFloat(parts[3]);
            float pitch = Float.parseFloat(parts[4]);
            return new BlockPos(x, y, z, yaw, pitch);
        } else {
            return new BlockPos(x, y, z, 0, 0);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BlockPos other)) return false;

        return this.x == other.x
              && this.y == other.y
              && this.z == other.z
              && this.yaw == other.yaw
              && this.pitch == other.pitch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, yaw, pitch);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public BlockPos clone() {
        return new BlockPos(x, y, z, yaw, pitch);
    }


    public Location asLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Vector asVector() {
        return new Vector(x, y, z);
    }

    public int[] asIntArray() {
        return new int[]{(int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z)};
    }


    public double distance(BlockPos other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public double distanceSquared(BlockPos other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public BlockPos add(BlockPos other) {
        return new BlockPos(this.x + other.x, this.y + other.y, this.z + other.z, this.yaw, this.pitch);
    }

    public BlockPos add(Vector vec) {
        return new BlockPos(this.x + vec.getX(), this.y + vec.getY(), this.z + vec.getZ(), this.yaw, this.pitch);
    }

    public BlockPos add(double x, double y, double z) {
        return new BlockPos(this.x + x, this.y + y, this.z + z, this.yaw, this.pitch);
    }

    public BlockPos subtract(BlockPos other) {
        return new BlockPos(this.x - other.x, this.y - other.y, this.z - other.z, this.yaw, this.pitch);
    }

    public BlockPos subtract(Vector vec) {
        return new BlockPos(this.x - vec.getX(), this.y - vec.getY(), this.z - vec.getZ(), this.yaw, this.pitch);
    }

    public BlockPos subtract(double x, double y, double z) {
        return new BlockPos(this.x - x, this.y - y, this.z - z, this.yaw, this.pitch);
    }

    public BlockPos multiply(BlockPos other) {
        return new BlockPos(this.x * other.x, this.y * other.y, this.z * other.z, this.yaw, this.pitch);
    }

    public BlockPos multiply(Vector vec) {
        return new BlockPos(this.x * vec.getX(), this.y * vec.getY(), this.z * vec.getZ(), this.yaw, this.pitch);
    }

    public BlockPos multiply(double scalar) {
        return new BlockPos(this.x * scalar, this.y * scalar, this.z * scalar, this.yaw, this.pitch);
    }

    public BlockPos divide(BlockPos other) {
        return new BlockPos(this.x / other.x, this.y / other.y, this.z / other.z, this.yaw, this.pitch);
    }

    public BlockPos divide(Vector vec) {
        return new BlockPos(this.x / vec.getX(), this.y / vec.getY(), this.z / vec.getZ(), this.yaw, this.pitch);
    }

    public BlockPos divide(double scalar) {
        return new BlockPos(this.x / scalar, this.y / scalar, this.z / scalar, this.yaw, this.pitch);
    }


    public BlockPos withYawPitch(PitchAndYaw pitchAndYaw) {
        return new BlockPos(this.x, this.y, this.z, pitchAndYaw.yaw, pitchAndYaw.pitch);
    }

    public BlockPos withYaw(float yaw) {
        return new BlockPos(this.x, this.y, this.z, yaw, this.pitch);
    }

    public BlockPos withPitch(float pitch) {
        return new BlockPos(this.x, this.y, this.z, this.yaw, pitch);
    }

    public BlockPos rotate(PitchAndYaw pitchAndYaw) {
        float newYaw = this.yaw + pitchAndYaw.yaw;
        float newPitch = this.pitch + pitchAndYaw.pitch;
        return new BlockPos(this.x, this.y, this.z, newYaw, newPitch);
    }

    public BlockPos rotateYaw(float degrees) {
        float newYaw = this.yaw + degrees;
        return new BlockPos(this.x, this.y, this.z, newYaw, this.pitch);
    }

    public BlockPos rotatePitch(float degrees) {
        float newPitch = this.pitch + degrees;
        return new BlockPos(this.x, this.y, this.z, this.yaw, newPitch);
    }

    public BlockPos rotate(Rotation rotation) {
        float newYaw = switch (rotation) {
            case CLOCKWISE_45 -> this.yaw + 45;
            case CLOCKWISE -> this.yaw + 90;
            case CLOCKWISE_135 -> this.yaw + 135;
            case FLIPPED -> this.yaw + 180;
            case FLIPPED_45 -> this.yaw + 270;
            case COUNTER_CLOCKWISE -> this.yaw - 90;
            case COUNTER_CLOCKWISE_45 -> this.yaw - 45;
            default -> this.yaw;
        };

        return new BlockPos(this.x, this.y, this.z, newYaw, this.pitch);
    }


}
