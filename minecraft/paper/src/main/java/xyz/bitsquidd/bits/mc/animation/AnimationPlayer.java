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

public abstract class AnimationPlayer {
    private final Animation animation;
    private int currentTick = 0;
    private int completedCycles = 0;
    private Consumer<AnimationPlayer> onComplete;

    private @Nullable BukkitTask ticker;

    public AnimationPlayer(Animation animation) {
        this.animation = animation;
    }

    public final void play() {
        ticker = Runnables.cleanup(ticker);
        ticker = Runnables.timer(this::tick, 0, animation.framerate);
    }

    public final void pause() {
        ticker = Runnables.cleanup(ticker);
    }

    public final void stop() {
        ticker = Runnables.cleanup(ticker);
        currentTick = 0;
        completedCycles = 0;
        onComplete.accept(this);
    }

    public final AnimationPlayer onComplete(Consumer<AnimationPlayer> callback) {
        this.onComplete = callback;
        return this;
    }

    public final void tick() {
        if (currentTick < animation.delayTicks) {
            currentTick++;
            return;
        }

        int animationTick = currentTick - animation.delayTicks;
        int duration = animation.durationTicks;
        currentTick++;

        float rawProgress = (float)animationTick / duration;

        switch (animation.loopMode) {
            case STRAIGHT -> {
                if (rawProgress >= 1f) {
                    completedCycles++;

                    if (animation.loopCount != -1 && completedCycles >= animation.loopCount) {
                        applyPose(1f);
                        stop();
                        return;
                    }
                    currentTick = animation.delayTicks;
                    rawProgress = 0f;
                }
            }
            case PING_PONG -> {
                int cycleLength = duration * 2;
                int cycleTick = animationTick % cycleLength;

                if (animationTick > 0 && animationTick % cycleLength == 0) {
                    completedCycles++;
                    if (animation.loopCount != -1 && completedCycles >= animation.loopCount) {
                        applyPose(0f);
                        stop();
                        return;
                    }
                }

                rawProgress = cycleTick < duration
                              ? (float)cycleTick / duration
                              : 1f - (float)(cycleTick - duration) / duration;
            }
        }

        applyPose(rawProgress);
    }

    private void applyPose(float progress) {
        AnimationPose pose = animation.evaluate(progress);
        applyPose(pose);
    }


    protected abstract void applyPose(AnimationPose pose);

}