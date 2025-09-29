package xyz.bitsquidd.bits.lib.helper.math;

import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathHelper {
    public static final double EPSILON = 1e-3;

    /**
     * Checks if two vectors are approximately equal, within a small epsilon.
     *
     * @param v1 the first vector
     * @param v2 the second vector
     *
     * @return true if the vectors are approximately equal, false otherwise
     */
    public static boolean vectorsEqual(Vector v1, Vector v2) {
        if (v1 == null || v2 == null) return false;

        return Math.abs(v1.getX() - v2.getX()) < EPSILON
              && Math.abs(v1.getY() - v2.getY()) < EPSILON
              && Math.abs(v1.getZ() - v2.getZ()) < EPSILON;
    }

    /**
     * Rounds a double to a certain number of decimal places.
     *
     * @param value  the value to round
     * @param places the number of decimal places to round to
     *
     * @return the rounded value
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
