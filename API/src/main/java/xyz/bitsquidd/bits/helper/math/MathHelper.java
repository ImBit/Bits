/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.helper.math;

import org.joml.Quaternionf;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A collection of useful mathematical operations.
 */
public final class MathHelper {
    private MathHelper() {}

    /**
     * Rounds a double to a certain number of decimal places.
     */
    public static double round(final double value, final int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static double sin(final double period, final double amplitude, final double phase) {
        return amplitude * Math.sin(phase * 2 * Math.PI / period);
    }

    public static double cos(final double period, final double amplitude, final double phase) {
        return amplitude * Math.cos(phase * 2 * Math.PI / period);
    }


    /**
     * Set of utility functions for working with quaternions.
     */
    public static final class Quaternion {
        public static float getXRotation(final Quaternionf quat) {
            return (float)Math.atan2(
              2.0 * (quat.w * quat.x + quat.z * quat.y),
              1.0 - 2.0 * (quat.x * quat.x + quat.y * quat.y)
            );
        }

        public static float getYRotation(final Quaternionf quat) {
            return (float)Math.atan2(
              2.0 * (quat.w * quat.y + quat.x * quat.z),
              1.0 - 2.0 * (quat.y * quat.y + quat.x * quat.x)
            );
        }

        public static float getZRotation(final Quaternionf quat) {
            return (float)Math.atan2(
              2.0 * (quat.w * quat.z + quat.y * quat.x),
              1.0 - 2.0 * (quat.x * quat.x + quat.z * quat.z)
            );
        }


        public static Quaternionf invertX(final Quaternionf quat) {
            return quat.rotationX(-getXRotation(quat));
        }

        public static Quaternionf invertY(final Quaternionf quat) {
            return quat.rotationY(-getYRotation(quat));
        }

        public static Quaternionf invertZ(final Quaternionf quat) {
            return quat.rotationZ(-getZRotation(quat));
        }


    }


}
