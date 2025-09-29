package xyz.bitsquidd.bits.lib.helper.math;

import org.jetbrains.annotations.Range;

public final class EasingHelper {
    private static final double EASING_MAGIC = 1.70158;

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
    public static double easeLinear(@Range(from = 0, to = 1) double t) {
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
    public static double easeIn(@Range(from = 0, to = 1) double t, int index) {
        return Math.pow(t, index);
    }

    /**
     * Ease out with a custom power
     */
    public static double easeOut(@Range(from = 0, to = 1) double t, int index) {
        return 1 - Math.pow(1 - t, index);
    }

    /**
     * Ease in-out with a custom power
     */
    public static double easeInOut(@Range(from = 0, to = 1) double t, int index) {
        if (t < 0.5) {
            return Math.pow(2 * t, index) / 2;
        } else {
            return 1 - Math.pow(-2 * t + 2, index) / 2;
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
    public static double easeInSine(@Range(from = 0, to = 1) double t) {
        return 1 - Math.cos((t * PI) / 2);
    }

    /**
     * Ease out sine function.
     */
    public static double easeOutSine(@Range(from = 0, to = 1) double t) {
        return Math.sin((t * PI) / 2);
    }

    /**
     * Ease in-out sine function.
     */
    public static double easeInOutSine(@Range(from = 0, to = 1) double t) {
        return -(Math.cos(PI * t) - 1) / 2;
    }


    // Exponential

    /**
     * Ease in exponential function.
     *
     * @param t the normalized time (0 to 1)
     *
     * @return the eased value
     */
    public static double easeInExpo(@Range(from = 0, to = 1) double t) {
        return t == 0 ? 0 : Math.pow(2, 10 * (t - 1));
    }

    /**
     * Ease out exponential function.
     */
    public static double easeOutExpo(@Range(from = 0, to = 1) double t) {
        return t == 1 ? 1 : 1 - Math.pow(2, -10 * t);
    }

    /**
     * Ease in-out exponential function.
     */
    public static double easeInOutExpo(@Range(from = 0, to = 1) double t) {
        if (t == 0) return 0;
        if (t == 1) return 1;
        if (t < 0.5) return Math.pow(2, 20 * t - 10) / 2;
        return (2 - Math.pow(2, -20 * t + 10)) / 2;
    }


    // Return back

    /**
     * Ease in back function. Creates an overshooting effect at the beginning.
     *
     * @param t the normalized time (0 to 1)
     *
     * @return the eased value
     */
    public static double easeInBack(@Range(from = 0, to = 1) double t) {
        double c3 = EASING_MAGIC + 1;
        return c3 * t * t * t - EASING_MAGIC * t * t;
    }

    /**
     * Ease out back function. Creates an overshooting effect at the end.
     */
    public static double easeOutBack(@Range(from = 0, to = 1) double t) {
        double c3 = EASING_MAGIC + 1;
        return 1 + c3 * Math.pow(t - 1, 3) + EASING_MAGIC * Math.pow(t - 1, 2);
    }


}
