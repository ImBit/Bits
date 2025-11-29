package xyz.bitsquidd.bits.lib.helper.math;

import org.jetbrains.annotations.Range;

public final class Easing {
    private static final float EASING_MAGIC = 1.70158f;

    private static final double PI = Math.PI;
    private static final double HALF_PI = PI / 2;
    private static final double TAU = 2 * PI;


    /// Linear
    /**
     * Linear easing function (no easing).
     *
     * @param t the normalized time (0 to 1)
     *
     * @return the eased value
     */
    public static float easeLinear(@Range(from = 0, to = 1) float t) {
        return t;
    }


    /// Curved

    /**
     * Ease in with a custom power
     *
     * @param t     the normalized time (0 to 1)
     * @param index the power to raise t to (e.g., 2 for quadratic, 3 for cubic)
     *
     * @return the eased value
     */
    public static float easeIn(@Range(from = 0, to = 1) float t, float index) {
        return (float)(Math.pow(t, index));
    }

    /**
     * Ease out with a custom power
     */
    public static float easeOut(@Range(from = 0, to = 1) float t, int index) {
        return (float)(1 - Math.pow(1 - t, index));
    }

    /**
     * Ease in-out with a custom power
     */
    public static float easeInOut(@Range(from = 0, to = 1) float t, float index) {
        if (t < 0.5) {
            return (float)(Math.pow(2 * t, index) / 2f);
        } else {
            return (float)(1 - Math.pow(-2 * t + 2, index) / 2f);
        }
    }


    /// Sine
    /**
     * Ease in sine function.
     *
     * @param t the normalized time (0 to 1)
     *
     * @return the eased value
     */
    public static float easeInSine(@Range(from = 0, to = 1) float t) {
        return (float)(1 - Math.cos((t * PI) / 2f));
    }

    /**
     * Ease out sine function.
     */
    public static float easeOutSine(@Range(from = 0, to = 1) float t) {
        return (float)(Math.sin((t * PI) / 2f));
    }

    /**
     * Ease in-out sine function.
     */
    public static float easeInOutSine(@Range(from = 0, to = 1) float t) {
        return (float)(-(Math.cos(PI * t) - 1) / 2f);
    }


    // Exponential

    /**
     * Ease in exponential function.
     *
     * @param t the normalized time (0 to 1)
     *
     * @return the eased value
     */
    public static float easeInExpo(@Range(from = 0, to = 1) float t) {
        return (float)(t == 0 ? 0f : Math.pow(2, 10 * (t - 1)));
    }

    /**
     * Ease out exponential function.
     */
    public static float easeOutExpo(@Range(from = 0, to = 1) float t) {
        return (float)(t == 1 ? 1f : 1 - Math.pow(2, -10 * t));
    }

    /**
     * Ease in-out exponential function.
     */
    public static float easeInOutExpo(@Range(from = 0, to = 1) float t) {
        if (t == 0) return 0;
        if (t == 1) return 1;
        if (t < 0.5) return (float)(Math.pow(2, 20 * t - 10) / 2);
        return (float)((2 - Math.pow(2, -20 * t + 10)) / 2f);
    }


    // Return back

    /**
     * Ease in back function. Creates an overshooting effect at the beginning.
     *
     * @param t the normalized time (0 to 1)
     *
     * @return the eased value
     */
    public static float easeInBack(@Range(from = 0, to = 1) float t) {
        double c3 = EASING_MAGIC + 1;
        return (float)(c3 * t * t * t - EASING_MAGIC * t * t);
    }

    /**
     * Ease out back function. Creates an overshooting effect at the end.
     */
    public static float easeOutBack(@Range(from = 0, to = 1) float t) {
        double c3 = EASING_MAGIC + 1;
        return (float)(1 + c3 * Math.pow(t - 1, 3) + EASING_MAGIC * Math.pow(t - 1, 2));
    }


}
