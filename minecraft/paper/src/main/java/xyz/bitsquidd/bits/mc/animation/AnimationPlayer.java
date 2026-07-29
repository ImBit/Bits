/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.animation;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.paper.util.bukkit.runnable.Runnables;

import java.util.function.Consumer;


public final class AnimationPlayer<A extends Animatable> {
    private final Animation animation;

    private long currentTick = 0;
    private @Nullable Consumer<AnimationPlayer<A>> onComplete;
    private @Nullable BukkitTask ticker;

    public AnimationPlayer(Animation animation) {
        this.animation = animation;
    }

    public final void play(A animatable) {
        ticker = Runnables.cleanup(ticker);
        currentTick = 0;
        ticker = Runnables.timer(() -> tick(animatable, currentTick++), 0, 1);
    }

    public final void stop() {
        ticker = Runnables.cleanup(ticker);
        currentTick = 0;
        if (onComplete != null) onComplete.accept(this);
    }

    public final AnimationPlayer<A> onComplete(Consumer<AnimationPlayer<A>> callback) {
        this.onComplete = callback;
        return this;
    }

    public final void tick(A animatable, long tick) {
        AnimationData data = new AnimationData(tick);

        if (animation.isFinished(data)) {
            stop();
            return;
        }

        // Fresh identity every tick: animation.mutate() computes this tick's complete snapshot,
        AnimationPoseNew pose = AnimationPoseNew.identity();
        animation.mutate(pose, data);
        animatable.applyPose(pose);
    }

}
