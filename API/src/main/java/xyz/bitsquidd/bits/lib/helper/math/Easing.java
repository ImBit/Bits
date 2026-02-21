/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.helper.math;

import org.jetbrains.annotations.Range;

/**
 * A collection of easing functions for smooth animations and transitions.
 * Each function takes a parameter t in the range [0, 1] and returns a transformed value in the same range.
 */
public final class Easing {
    private Easing() {}

    private static final double EASING_MAGIC = 1.70158;

    private static final double PI = Math.PI;
    private static final double TAU = 2 * PI;
    private static final double HALF_PI = PI / 2;


    /// Linear
    /**
     * Linear easing function (no easing).
     */
    public static float linear(@Range(from = 0, to = 1) float t) {
        return t;
    }


    /// Curved

    /**
     * Ease in with a custom power
     *
     * @param index the power to raise t to (e.g., 2 for quadratic, 3 for cubic)
     */
    public static float in(@Range(from = 0, to = 1) float t, float index) {
        return (float)(Math.pow(t, index));
    }

    /**
     * Default ease in (quadratic)
     */
    public static float in(@Range(from = 0, to = 1) float t) {
        return in(t, 2);
    }

    /**
     * Ease out with a custom power
     */
    public static float out(@Range(from = 0, to = 1) float t, float index) {
        return (float)(1 - Math.pow(1 - t, index));
    }

    /**
     * Default ease out (quadratic)
     */
    public static float out(@Range(from = 0, to = 1) float t) {
        return out(t, 2);
    }

    /**
     * Ease in-out with a custom power
     */
    public static float inOut(@Range(from = 0, to = 1) float t, float index) {
        if (t < 0.5) {
            return in(t * 2, index) / 2f;
        } else {
            return out((1 - t) * 2, index) / 2f + 0.5f;
        }
    }

    /**
     * Blends a linear transition with an ease in-out transition.
     * Note: a multiplier of 0 results in a linear transition, while a multiplier of 1 results in a full ease in-out transition.
     */
    public static float inOutSmooth(float t, float index, @Range(from = 0, to = 1) float multiplier) {
        float eased = inOut(t, index);
        return (1 - multiplier) * t + multiplier * eased;
    }


    /// Sine
    /**
     * Ease in sine function.
     */
    public static float inSin(@Range(from = 0, to = 1) float t) {
        return (float)(1 - Math.cos((t * PI) / 2f));
    }

    /**
     * Ease out sine function.
     */
    public static float outSin(@Range(from = 0, to = 1) float t) {
        return (float)(Math.sin((t * PI) / 2f));
    }

    /**
     * Ease in-out sine function.
     */
    public static float inOutSin(@Range(from = 0, to = 1) float t) {
        return (float)(-(Math.cos(PI * t) - 1) / 2f);
    }


    // Exponential

    /**
     * Ease in exponential function.
     */
    public static float inExpo(@Range(from = 0, to = 1) float t) {
        return (float)(t == 0 ? 0f : Math.pow(2, 10 * (t - 1)));
    }

    /**
     * Ease out exponential function.
     */
    public static float outExpo(@Range(from = 0, to = 1) float t) {
        return (float)(t == 1 ? 1f : 1 - Math.pow(2, -10 * t));
    }

    /**
     * Ease in-out exponential function.
     */
    public static float inOutExpo(@Range(from = 0, to = 1) float t) {
        if (t <= 0) return 0;
        if (t >= 1) return 1;
        if (t < 0.5) return (float)(Math.pow(2, 20 * t - 10) / 2);
        return (float)((2 - Math.pow(2, -20 * t + 10)) / 2f);
    }


    // Return back

    /**
     * Ease in back function. Creates an overshooting effect at the beginning.
     */
    public static float inBack(@Range(from = 0, to = 1) float t) {
        return (float)((EASING_MAGIC + 1) * t * t * t - EASING_MAGIC * t * t);
    }

    /**
     * Ease out back function. Creates an overshooting effect at the end.
     */
    public static float outBack(@Range(from = 0, to = 1) float t) {
        return (float)(1 + (EASING_MAGIC + 1) * Math.pow(t - 1, 3) + EASING_MAGIC * Math.pow(t - 1, 2));
    }

    /**
     * Ease in-out back function. Creates an overshooting effect at both the beginning and end.
     */
    public static float inOutBack(@Range(from = 0, to = 1) float t) {
        double c = EASING_MAGIC * 1.525;

        if (t <= 0) return 0;
        if (t >= 1) return 1;
        if (t < 0.5) return (float)(Math.pow(2 * t, 2) * ((c + 1) * 2 * t - c)) / 2;
        return (float)(Math.pow(2 * t - 2, 2) * ((c + 1) * (t * 2 - 2) + c) + 2) / 2;
    }


    // Circular

    /**
     * Circular ease in - accelerating from zero velocity.
     */
    public static float inCirc(@Range(from = 0, to = 1) float t) {
        return 1 - (float)Math.sqrt(1 - t * t);
    }

    /**
     * Circular ease out - decelerating to zero velocity.
     */
    public static float outCirc(@Range(from = 0, to = 1) float t) {
        return (float)Math.sqrt(1 - Math.pow(t - 1, 2));
    }

    /**
     * Circular ease in-out - acceleration until halfway, then deceleration.
     */
    public static float inOutCirc(@Range(from = 0, to = 1) float t) {
        if (t < 0.5) return (1 - (float)Math.sqrt(1 - Math.pow(2 * t, 2))) / 2;
        return ((float)Math.sqrt(1 - Math.pow(-2 * t + 2, 2)) + 1) / 2;
    }


    // Elastic

    /**
     * Elastic ease in - creates a spring-like effect at the beginning.
     */
    public static float inElastic(@Range(from = 0, to = 1) float t) {
        double c = TAU / 3;

        if (t == 0) return 0;
        if (t == 1) return 1;
        return (float)(-Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * c));
    }

    /**
     * Elastic ease out - creates a spring-like effect at the end.
     */
    public static float outElastic(@Range(from = 0, to = 1) float t) {
        double c = TAU / 3;

        if (t == 0) return 0;
        if (t == 1) return 1;
        return (float)(Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * c) + 1);
    }

    /**
     * Elastic ease in-out - creates a spring-like effect at both the beginning and end.
     */
    public static float inOutElastic(@Range(from = 0, to = 1) float t) {
        double c = TAU / 4.5;

        if (t == 0) return 0;
        if (t == 1) return 1;
        if (t < 0.5) {
            return (float)(-(Math.pow(2, 20 * t - 10) * Math.sin((20 * t - 11.125) * c)) / 2);
        }
        return (float)((Math.pow(2, -20 * t + 10) * Math.sin((20 * t - 11.125) * c)) / 2 + 1);
    }


    // Bounce

    /**
     * Bounce ease in - creates a bouncing effect at the beginning.
     */
    public static float inBounce(@Range(from = 0, to = 1) float t) {
        return 1 - outBounce(1 - t);
    }

    /**
     * Bounce ease out - creates a bouncing effect at the end.
     */
    public static float outBounce(@Range(from = 0, to = 1) float t) {
        double c1 = 7.5625;
        double c2 = 2.75;

        if (t < 1 / c2) {
            return (float)(c1 * t * t);
        } else if (t < 2 / c2) {
            return (float)(c1 * (t -= (float)(1.5 / c2)) * t + 0.75);
        } else if (t < 2.5 / c2) {
            return (float)(c1 * (t -= (float)(2.25 / c2)) * t + 0.9375);
        } else {
            return (float)(c1 * (t -= (float)(2.625 / c2)) * t + 0.984375);
        }
    }

    /**
     * Bounce ease in-out - creates a bouncing effect at both the beginning and end.
     */
    public static float inOutBounce(@Range(from = 0, to = 1) float t) {
        if (t < 0.5) return inBounce(t * 2) / 2;
        return (outBounce(t * 2 - 1) + 1) / 2;
    }

}
