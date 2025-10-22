package xyz.bitsquidd.bits.lib.location;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PitchAndYaw {
    public final float pitch;
    public final float yaw;

    private PitchAndYaw(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public static PitchAndYaw of(float pitch, float yaw) {
        return new PitchAndYaw(pitch, yaw);
    }

    public void applyToLocation(Location location) {
        location.setPitch(pitch);
        location.setYaw(yaw);
    }
    
}
