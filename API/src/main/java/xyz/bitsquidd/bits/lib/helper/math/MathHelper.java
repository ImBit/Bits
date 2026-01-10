package xyz.bitsquidd.bits.lib.helper.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A collection of useful mathematical operations.
 */
public final class MathHelper {

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
