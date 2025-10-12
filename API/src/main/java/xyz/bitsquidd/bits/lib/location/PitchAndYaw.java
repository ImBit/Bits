package xyz.bitsquidd.bits.lib.location;

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
}
