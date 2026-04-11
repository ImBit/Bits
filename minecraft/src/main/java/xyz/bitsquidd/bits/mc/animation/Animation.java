/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.animation;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Range;

import xyz.bitsquidd.bits.util.math.easing.Easing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Animation {
    public final ImmutableList<Keyframe> keyframes;
    public final int durationTicks;
    public final AnimationMode loopMode;
    public final int loopCount;
    public final int delayTicks;
    public final int framerate;

    private Animation(Builder builder) {
        // Sort keyframes by progress so evaluation is always ordered
        this.keyframes = ImmutableList.copyOf(builder.keyframes.stream()
          .sorted(Comparator.comparingDouble(Keyframe::progress))
          .toList());
        this.durationTicks = builder.durationTicks;
        this.loopMode = builder.loopMode;
        this.loopCount = builder.loopCount;
        this.delayTicks = builder.delayTicks;
        this.framerate = builder.framerate;
    }

    public AnimationPose evaluate(@Range(from = 0, to = 1) float progress) {
        if (keyframes.isEmpty()) return AnimationPose.identity();
        if (keyframes.size() == 1) return keyframes.getFirst().pose();

        // Clamp to first/last keyframe outside the defined range
        if (progress <= keyframes.getFirst().progress()) return keyframes.getFirst().pose();
        if (progress >= keyframes.get(keyframes.size() - 1).progress()) {
            return keyframes.get(keyframes.size() - 1).pose();
        }

        // Find the surrounding keyframe pair
        Keyframe from = keyframes.get(0);
        Keyframe to = keyframes.get(1);

        for (int i = 0; i < keyframes.size() - 1; i++) {
            if (keyframes.get(i).progress() <= progress && keyframes.get(i + 1).progress() >= progress) {
                from = keyframes.get(i);
                to = keyframes.get(i + 1);
                break;
            }
        }

        // Normalise progress within this keyframe interval
        float intervalLength = to.progress() - from.progress();
        float localProgress = (progress - from.progress()) / intervalLength;

        // Apply the easing defined on the FROM keyframe
        float easedProgress = from.easing().progress(localProgress);

        return AnimationPose.interpolate(from.pose(), to.pose(), easedProgress);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final List<Keyframe> keyframes = new ArrayList<>();
        private int durationTicks = 20;
        private AnimationMode loopMode = AnimationMode.STRAIGHT;
        private int loopCount = -1;
        private int delayTicks = 0;
        private int framerate = 1;

        public Builder duration(int ticks) {
            this.durationTicks = ticks;
            return this;
        }

        public Builder loop(AnimationMode mode) {
            this.loopMode = mode;
            return this;
        }

        public Builder repeat(int count) {
            this.loopCount = count;
            return this;
        }

        public Builder delay(int ticks) {
            this.delayTicks = ticks;
            return this;
        }

        public Builder framerate(int framesPerSecond) {
            this.framerate = framesPerSecond;
            return this;
        }

        public Builder keyframe(Keyframe keyframe) {
            this.keyframes.add(keyframe);
            return this;
        }

        public Builder keyframe(float progress, AnimationPose pose) {
            return keyframe(Keyframe.of(progress, pose));
        }

        public Builder keyframe(float progress, AnimationPose pose, Easing easing) {
            return keyframe(Keyframe.of(progress, pose, easing));
        }

        public Animation build() {
            if (keyframes.isEmpty()) throw new IllegalStateException("Animation requires at least 1 keyframe");
            return new Animation(this);
        }

    }

}