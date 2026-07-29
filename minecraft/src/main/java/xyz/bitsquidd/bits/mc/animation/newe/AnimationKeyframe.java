/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.animation.newe;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Function;


public sealed interface AnimationKeyframe {

    void applyTo(AnimationPoseNew pose, AnimationData data, float proportion);

    record Translation(
      Function<AnimationData, Vector3f> translation
    ) implements AnimationKeyframe {

        @Override
        public void applyTo(AnimationPoseNew pose, AnimationData data, float proportion) {
            Vector3f effective = translation.apply(data).lerp(new Vector3f(), 1 - proportion);
            pose.translation().add(effective); // Translation combines additively
        }

    }

    record Rotation(
      Function<AnimationData, Quaternionf> rotation
    ) implements AnimationKeyframe {

        @Override
        public void applyTo(AnimationPoseNew pose, AnimationData data, float proportion) {
            Quaternionf effective = rotation.apply(data).slerp(new Quaternionf(), 1 - proportion);
            pose.rotation().mul(effective); // Rotation combines multiplicatively
        }

    }

    record Scale(
      Function<AnimationData, Vector3f> scale
    ) implements AnimationKeyframe {

        @Override
        public void applyTo(AnimationPoseNew pose, AnimationData data, float proportion) {
            Vector3f effective = scale.apply(data).lerp(new Vector3f(1), 1 - proportion);
            pose.scale().mul(effective); // Scale combines multiplicatively
        }

    }


}
