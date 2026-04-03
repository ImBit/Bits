/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.util.location.util;

import org.joml.Quaternionf;

public enum RotationRounding {
    NONE,          // Nothing applied
    ZERO,          // No rotation at all.
    ZERO_XZ,       // Only rotate Y.
    NEAREST_90,    // Rotate Y to the nearest 90 degrees.
    NEAREST_45,    // Rotate Y to the nearest 45 degrees.
    NEAREST_225,   // Rotate Y to the nearest 22.5 degrees.
    BLOCKFACE_90,  // Rotate to the nearest blockface.
    ;

    public final Quaternionf applyTo(Quaternionf rotation) {
        return switch (this) {
            case BLOCKFACE_90 -> {
                float yaw = 2 * (float)Math.atan2(rotation.y, rotation.w);
                yaw = (yaw % (2 * (float)Math.PI) + 2 * (float)Math.PI) % (2 * (float)Math.PI);
                float snappedYaw = Math.round(yaw / ((float)Math.PI / 2)) * ((float)Math.PI / 2);

                float pitch = (float)Math.asin(2 * (rotation.w * rotation.x - rotation.y * rotation.z));
                float snappedPitch = Math.round(pitch / ((float)Math.PI / 2)) * ((float)Math.PI / 2);

                rotation.identity().rotateY(snappedYaw).rotateX(snappedPitch);
                yield rotation;
            }
            case NEAREST_90 -> {
                float yaw = 2 * (float)Math.atan2(rotation.y, rotation.w);
                yaw = (yaw % (2 * (float)Math.PI) + 2 * (float)Math.PI) % (2 * (float)Math.PI);
                float roundedYaw = Math.round(yaw / ((float)Math.PI / 2)) * ((float)Math.PI / 2);
                rotation.identity().rotateY(roundedYaw);
                yield rotation;
            }
            case NEAREST_45 -> {
                float yaw = 2 * (float)Math.atan2(rotation.y, rotation.w);
                yaw = (yaw % (2 * (float)Math.PI) + 2 * (float)Math.PI) % (2 * (float)Math.PI);
                float roundedYaw = Math.round(yaw / ((float)Math.PI / 4)) * ((float)Math.PI / 4);
                rotation.identity().rotateY(roundedYaw);
                yield rotation;
            }
            case NEAREST_225 -> {
                float yaw = 2 * (float)Math.atan2(rotation.y, rotation.w);
                yaw = (yaw % (2 * (float)Math.PI) + 2 * (float)Math.PI) % (2 * (float)Math.PI);
                float roundedYaw = Math.round(yaw / ((float)Math.PI / 8)) * ((float)Math.PI / 8);
                rotation.identity().rotateY(roundedYaw);
                yield rotation;
            }
            case ZERO_XZ -> {
                float yaw = 2 * (float)Math.atan2(rotation.y, rotation.w);
                rotation.identity().rotateY(yaw);
                yield rotation;
            }
            case ZERO -> {
                rotation.rotationX(0);
                rotation.rotationY(0);
                rotation.rotationZ(0);
                yield rotation;
            }
            default -> rotation;
        };
    }
}
