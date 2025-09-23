package xyz.bitsquidd.bits.lib.sendable.sound;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.location.BlockPos;
import xyz.bitsquidd.bits.lib.sendable.Sendable;

//TODO make it nullable safe with default values.
public class BitSound implements Sendable {
    protected final @NotNull NamespacedKey soundKey;
    protected final float volume;
    protected final float pitch;
    protected final @Nullable BlockPos blockPos;
    protected final @Nullable Sound.Source soundSource;

    public BitSound(@NotNull NamespacedKey soundKey, float volume, float pitch, @Nullable BlockPos blockPos, @Nullable Sound.Source soundSource) {
        this.soundKey = soundKey;
        this.volume = volume;
        this.pitch = pitch;
        this.blockPos = blockPos;
        this.soundSource = soundSource;
    }

    @Override
    public <T extends Audience> void send(@NotNull T target) {
        target.playSound(Sound.sound(soundKey, soundSource, pitch, volume), blockPos.x, blockPos.y, blockPos.z);
    }
}
