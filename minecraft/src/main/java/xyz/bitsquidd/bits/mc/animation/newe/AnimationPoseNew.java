/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.animation.newe;

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

    public void add(AnimationKeyframe added) {
        Vector3f addedTranslation = added.translation();
        Quaternionf addedRotation = added.rotation();
        Vector3f addedScale = added.scale();

        if (addedTranslation != null) translation.add(addedTranslation); // Translation combines additively
        if (addedRotation != null) rotation.mul(addedRotation);          // Rotation combines multiplicatively
        if (addedScale != null) scale.mul(addedScale);                   // Scale combines multiplicatively
    }

}
