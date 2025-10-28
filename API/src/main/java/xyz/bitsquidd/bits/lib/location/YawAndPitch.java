package xyz.bitsquidd.bits.lib.location;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class YawAndPitch {
    public final float yaw;
    public final float pitch;

    private YawAndPitch(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static YawAndPitch of(float yaw, float pitch) {
        return new YawAndPitch(pitch, yaw);
    }

    public void applyToLocation(Location location) {
        location.setPitch(pitch);
        location.setYaw(yaw);
    }

}
