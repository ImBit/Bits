package xyz.bitsquidd.bits.lib.wrappers;

/**
 * An immutable class representing number inbetween 0 and 1 representing a percentage.
 */
public final class Percentage {
    private final float value;

    private Percentage(final double value) {
        // We cast to float, as that's more than precise enough for our needs and allows for flexibility with inputs.
        this.value = (float)Math.clamp(value, 0.0, 1.0);
    }

    public static Percentage ZERO = new Percentage(0.0f);
    public static Percentage FULL = new Percentage(1.0f);

    public static Percentage of(final double value) {
        return new Percentage(value);
    }

    public static Percentage ofFraction(final double numerator, final double denominator) {
        if (denominator == 0.0) return ZERO;
        return new Percentage(numerator / denominator);
    }

    public static Percentage ofFractionReverse(final double denominator, final double numerator) {
        if (numerator == 0.0) return FULL;
        return new Percentage(1 - (numerator / denominator));
    }

    public Percentage add(final Percentage other) {
        return new Percentage(this.value + other.value);
    }

    public Percentage subtract(final Percentage other) {
        return new Percentage(this.value - other.value);
    }

    public float get() {
        return value;
    }

}
