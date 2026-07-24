/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.animation;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.mc.animation.impl.Animation;
import xyz.bitsquidd.bits.paper.util.bukkit.runnable.Runnables;

import java.util.function.Consumer;


// TODO
//  Refine animation system.
public abstract class AnimationPlayer<D> {
    private final Animation animation;

    private int currentTick = 0;
    private @Nullable Consumer<AnimationPlayer<D>> onComplete;
    private @Nullable BukkitTask ticker;

    public AnimationPlayer(Animation animation) {
        this.animation = animation;
    }

    public final void play(D display) {
        ticker = Runnables.cleanup(ticker);
        ticker = Runnables.timer(() -> tick(display, currentTick++), 0, 1);
    }

    public final void pause() {
        ticker = Runnables.cleanup(ticker);
    }

    public final void stop() {
        ticker = Runnables.cleanup(ticker);
        currentTick = 0;
        if (onComplete != null) onComplete.accept(this);
    }

    public final AnimationPlayer<D> onComplete(Consumer<AnimationPlayer<D>> callback) {
        this.onComplete = callback;
        return this;
    }

    public final void tick(D display, int tick) {
        if (animation.isFinished(tick)) {
            stop();
            return;
        }

        AnimationPose pose = animation.evaluate(tick);
        applyPose(display, pose);
    }

    protected abstract void applyPose(D display, AnimationPose pose);

}