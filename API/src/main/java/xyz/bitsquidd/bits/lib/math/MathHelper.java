package xyz.bitsquidd.bits.lib.math;

import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathHelper {
    public static final double vecEpsilon = 1e-3;

    public static boolean vectorsEqual(Vector v1, Vector v2) {
        if (v1 == null || v2 == null) return false;

        return Math.abs(v1.getX() - v2.getX()) < vecEpsilon
              && Math.abs(v1.getY() - v2.getY()) < vecEpsilon
              && Math.abs(v1.getZ() - v2.getZ()) < vecEpsilon;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
