package xyz.bitsquidd.bits.lib.sendable.sound;

public final class PitchAndVolume {
    public final float pitch;
    public final float volume;

    private PitchAndVolume(float pitch, float volume) {
        this.pitch = pitch;
        this.volume = volume;
    }

    public static PitchAndVolume of(float pitch, float volume) {
        return new PitchAndVolume(pitch, volume);
    }
}
