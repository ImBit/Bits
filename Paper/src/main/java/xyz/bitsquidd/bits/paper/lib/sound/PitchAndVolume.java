/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.lib.sound;

/**
 * Immutable class representing pitch and volume values for sounds.
 */
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
