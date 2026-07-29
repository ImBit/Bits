/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.animation;

import org.joml.Quaternionf;
import org.joml.Vector3f;


public interface Animatable {
    Vector3f getTranslation();

    Quaternionf getRotation();

    Vector3f getScale();


    void applyPose(AnimationPoseNew pose);

}
