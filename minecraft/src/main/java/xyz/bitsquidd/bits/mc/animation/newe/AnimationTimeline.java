/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.animation.newe;

import xyz.bitsquidd.bits.lifecycle.builder.Buildable;
import xyz.bitsquidd.bits.util.math.easing.Easing;
import xyz.bitsquidd.bits.wrapper.collection.pair.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class AnimationTimeline {
    private record KeyframeRecord(
      List<Pair<AnimationKeyframe, Float>> keyframes
      // Keyframe and its proportion
    ) {}

    private final List<KeyframeRecord> baked;

    private AnimationTimeline(List<KeyframeRecord> baked) {
        this.baked = baked;
    }


    public void mutate(AnimationPoseNew pose, AnimationData data) {
        int effectiveIndex = (int)data.currentTick() % baked.size();
        KeyframeRecord record = baked.get(effectiveIndex);
        record.keyframes().forEach(pair -> pair.getFirst().applyTo(pose, data, pair.getSecond()));
    }


    public static final class Builder implements Buildable<AnimationTimeline> {
        private final long ticks;
        private final List<TimelineEntry> entries = new ArrayList<>();

        public Builder(long ticks) {
            this.ticks = ticks;
        }

        private record TimelineEntry(
          long effectiveTick,
          AnimationKeyframe frame,
          Easing easing
        ) {}


        public Builder add(float proportion, AnimationKeyframe frame, Easing easing) {
            entries.add(new TimelineEntry((long)(proportion * ticks), frame, easing));
            return this;
        }


        @Override
        public AnimationTimeline build() {
            if (entries.isEmpty()) throw new IllegalStateException("Cannot build an empty AnimationTimeline");

            List<TimelineEntry> sorted = new ArrayList<>(entries);
            sorted.sort(Comparator.comparingLong(TimelineEntry::effectiveTick));

            TimelineEntry first = sorted.getFirst();
            TimelineEntry last = sorted.getLast();

            List<KeyframeRecord> baked = new ArrayList<>((int)ticks);
            int segment = 0;

            for (long t = 0; t < ticks; t++) {
                if (t <= first.effectiveTick()) {
                    baked.add(new KeyframeRecord(List.of(Pair.immutable(first.frame(), 1f))));
                    continue;
                }
                if (t >= last.effectiveTick()) {
                    baked.add(new KeyframeRecord(List.of(Pair.immutable(last.frame(), 1f))));
                    continue;
                }

                while (segment < sorted.size() - 2 && t > sorted.get(segment + 1).effectiveTick()) {
                    segment++;
                }

                TimelineEntry from = sorted.get(segment);
                TimelineEntry to = sorted.get(segment + 1);
                long span = to.effectiveTick() - from.effectiveTick();
                float local = span == 0 ? 1f : (t - from.effectiveTick()) / (float)span;
                float eased = to.easing().progress(local);

                baked.add(new KeyframeRecord(List.of(
                  Pair.immutable(from.frame(), 1f - eased),
                  Pair.immutable(to.frame(), eased)
                )));
            }

            return new AnimationTimeline(baked);
        }

    }


}
