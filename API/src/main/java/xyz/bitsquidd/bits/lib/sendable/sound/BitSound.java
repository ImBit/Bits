package xyz.bitsquidd.bits.lib.sendable.sound;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.location.BlockPos;
import xyz.bitsquidd.bits.lib.sendable.Sendable;

//TODO make it nullable safe with default values.
@NullMarked
public class BitSound implements Sendable {
    protected final NamespacedKey soundKey;
    protected final @Nullable PitchAndVolume pitchAndVolume;
    protected final @Nullable BlockPos blockPos;
    protected final @Nullable Sound.Source soundSource;


    private static final PitchAndVolume DEFAULT_PITCH_AND_VOLUME = PitchAndVolume.of(1.0f, 1.0f);
    private static final BlockPos DEFAULT_BLOCK_POS = BlockPos.ORIGIN;
    private static final Sound.Source DEFAULT_SOUND_SOURCE = Sound.Source.MASTER;

    public BitSound(NamespacedKey soundKey, @Nullable PitchAndVolume pitchAndVolume, @Nullable BlockPos blockPos, @Nullable Sound.Source soundSource) {
        this.soundKey = soundKey;
        this.pitchAndVolume = pitchAndVolume;
        this.blockPos = blockPos;
        this.soundSource = soundSource;
    }

    @Override
    public <T extends Audience> void send(@NotNull T target) {
        PitchAndVolume targetPitchAndVolue = this.pitchAndVolume != null ? this.pitchAndVolume : DEFAULT_PITCH_AND_VOLUME;
        BlockPos targetPos = blockPos != null ? blockPos : (target instanceof Entity entity ? BlockPos.of(entity) : DEFAULT_BLOCK_POS);
        Sound.Source soundSource = this.soundSource != null ? this.soundSource : DEFAULT_SOUND_SOURCE;

        target.playSound(
              Sound.sound(soundKey, soundSource, targetPitchAndVolue.pitch, targetPitchAndVolue.volume),
              targetPos.x, targetPos.y, targetPos.z
        );
    }
}
