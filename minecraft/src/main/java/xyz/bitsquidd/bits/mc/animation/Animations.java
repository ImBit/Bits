/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.animation;

import xyz.bitsquidd.bits.mc.animation.impl.Animation;
import xyz.bitsquidd.bits.util.math.easing.Easings;


public final class Animations {
    private Animations() {}

    //region Generic transformations
    public static Animation translation(float x, float y, float z) {
        return Animation.constant(AnimationPose.builder().translation(x, y, z).build());
    }

    public static Animation xTranslation(float distance) {
        return Animation.constant(AnimationPose.builder().translateX(distance).build());
    }

    public static Animation yTranslation(float height) {
        return Animation.constant(AnimationPose.builder().translateY(height).build());
    }

    public static Animation zTranslation(float distance) {
        return Animation.constant(AnimationPose.builder().translateZ(distance).build());
    }

    public static Animation scale(float scale) {
        return Animation.constant(AnimationPose.builder().scale(scale).build());
    }

    public static Animation scale(float x, float y, float z) {
        return Animation.constant(AnimationPose.builder().scale(x, y, z).build());
    }

    public static Animation xScale(float scale) {
        return Animation.constant(AnimationPose.builder().scaleX(scale).build());
    }

    public static Animation yScale(float scale) {
        return Animation.constant(AnimationPose.builder().scaleY(scale).build());
    }

    public static Animation zScale(float scale) {
        return Animation.constant(AnimationPose.builder().scaleZ(scale).build());
    }

    public static Animation rotation(float x, float y, float z) {
        return Animation.constant(AnimationPose.builder().rotateX(x).rotateY(y).rotateZ(z).build());
    }

    public static Animation xRotation(float angle) {
        return Animation.constant(AnimationPose.builder().rotateX(angle).build());
    }

    public static Animation yRotation(float angle) {
        return Animation.constant(AnimationPose.builder().rotateY(angle).build());
    }

    public static Animation zRotation(float angle) {
        return Animation.constant(AnimationPose.builder().rotateZ(angle).build());
    }
    //endregion

    //region Floating
    public static Animation floating() {
        return floating(40, 0.5f);
    }

    public static Animation floating(int duration, float height) {
        return Animation.of()
          .duration(duration)
          .loop(AnimationLoopMode.PING_PONG)
          .keyframe(0.00f, AnimationPose.builder().translateY(0f).build(), Easings.IN_OUT_SIN)
          .keyframe(1.00f, AnimationPose.builder().translateY(height).build(), Easings.IN_OUT_SIN)
          .build();
    }
    //endregion


    public static Animation spin(int duration) {
        return Animation.of()
          .duration(duration)
          .loop(AnimationLoopMode.STRAIGHT)
          .keyframe(0.00f, AnimationPose.builder().rotateY(0f).build(), Easings.IN_OUT_SIN.blend(Easings.LINEAR, 0.5f))
          .keyframe(0.50f, AnimationPose.builder().rotateY(180f).build(), Easings.IN_OUT_SIN.blend(Easings.LINEAR, 0.5f))
          .keyframe(1.00f, AnimationPose.builder().rotateY(360f).build(), Easings.IN_OUT_SIN.blend(Easings.LINEAR, 0.5f))
          .build();
    }

    public static Animation pulse(int duration, float minScale, float maxScale) {
        return Animation.of()
          .duration(duration)
          .loop(AnimationLoopMode.PING_PONG)
          .keyframe(0.00f, AnimationPose.builder().scale(minScale).build(), Easings.IN_OUT_SIN)
          .keyframe(1.00f, AnimationPose.builder().scale(maxScale).build(), Easings.IN_OUT_SIN)
          .build();
    }


    public static Animation swayX(int duration, float angle) {
        return Animation.of()
          .duration(duration)
          .loop(AnimationLoopMode.PING_PONG)
          .keyframe(0.00f, AnimationPose.builder().rotateX(-angle).build(), Easings.IN_OUT_SIN)
          .keyframe(1.00f, AnimationPose.builder().rotateX(angle).build(), Easings.IN_OUT_SIN)
          .build();
    }

    public static Animation swayZ(int duration, float angle) {
        return Animation.of()
          .duration(duration)
          .loop(AnimationLoopMode.PING_PONG)
          .keyframe(0.00f, AnimationPose.builder().rotateZ(-angle).build(), Easings.IN_OUT_SIN)
          .keyframe(1.00f, AnimationPose.builder().rotateZ(angle).build(), Easings.IN_OUT_SIN)
          .build();
    }

    public static Animation wiggleX(int duration, float amplitude) {
        return Animation.of()
          .duration(duration)
          .loop(AnimationLoopMode.PING_PONG)
          .keyframe(0.00f, AnimationPose.builder().translateX(-amplitude).build(), Easings.IN_OUT_SIN)
          .keyframe(1.00f, AnimationPose.builder().translateX(amplitude).build(), Easings.IN_OUT_SIN)
          .build();
    }

    public static Animation wiggleZ(int duration, float amplitude) {
        return Animation.of()
          .duration(duration)
          .loop(AnimationLoopMode.PING_PONG)
          .keyframe(0.00f, AnimationPose.builder().translateZ(-amplitude).build(), Easings.IN_OUT_SIN)
          .keyframe(1.00f, AnimationPose.builder().translateZ(amplitude).build(), Easings.IN_OUT_SIN)
          .build();
    }

    public static Animation zoomIn(int duration) {
        return zoomIn(duration, 0f);
    }

    public static Animation zoomIn(int duration, float startScale) {
        return Animation.of()
          .duration(duration)
          .keyframe(0.00f, AnimationPose.builder().scale(startScale).build(), Easings.OUT_BACK)
          .keyframe(1.00f, AnimationPose.builder().scale(1f).build(), Easings.OUT_BACK)
          .build();
    }

    public static Animation zoomOut(int duration) {
        return zoomOut(duration, 0f);
    }

    public static Animation zoomOut(int duration, float endScale) {
        return Animation.of()
          .duration(duration)
          .keyframe(0.00f, AnimationPose.builder().scale(1f).build(), Easings.IN_BACK)
          .keyframe(1.00f, AnimationPose.builder().scale(endScale).build(), Easings.IN_BACK)
          .build();
    }

    public static Animation moveUp(int duration, float distance) {
        return Animation.of()
          .duration(duration)
          .keyframe(0.00f, AnimationPose.builder().translateY(-distance).build(), Easings.OUT_BACK)
          .keyframe(1.00f, AnimationPose.builder().translateY(0).build(), Easings.OUT_BACK)
          .build();
    }

    public static Animation moveUp(int duration) {
        return moveUp(duration, 1.0f);
    }

    public static Animation moveDown(int duration, float distance) {
        return Animation.of()
          .duration(duration)
          .keyframe(0.00f, AnimationPose.builder().translateY(0).build(), Easings.IN_BACK)
          .keyframe(1.00f, AnimationPose.builder().translateY(-distance).build(), Easings.IN_BACK)
          .build();
    }

    public static Animation moveDown(int duration) {
        return moveDown(duration, 1.0f);
    }


    //region Composite Animations
    public static Animation floatSpin() {
        return floating(40, 0.5f).and(spin(30));
    }

    public static Animation hotAirBalloon() {
        return floating(400, 1.0f)      // slow rise and fall
          .and(wiggleX(80, 0.15f))   // drift X
          .and(wiggleZ(100, 0.09f))  // drift Z - slight different duration to give an offset feel
          .and(swayX(90, 2.0f))         // atmospheric tilt X
          .and(swayZ(110, 1.5f))        // atmospheric tilt Z
          .and(spin(2000));
    }
    //endregion

}
