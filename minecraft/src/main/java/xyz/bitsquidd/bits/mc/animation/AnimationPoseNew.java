/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.animation;

import org.joml.Quaternionf;
import org.joml.Vector3f;


public record AnimationPoseNew(
  Vector3f translation,
  Quaternionf rotation,
  Vector3f scale
) {

    public static AnimationPoseNew identity() {
        return new AnimationPoseNew(
          new Vector3f(0),  // No translation
          new Quaternionf(),  // No rotation
          new Vector3f(1)  // No scaling
        );
    }

}
