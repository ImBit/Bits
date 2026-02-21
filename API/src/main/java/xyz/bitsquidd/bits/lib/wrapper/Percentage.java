/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.wrapper;

import xyz.bitsquidd.bits.lib.helper.math.MathHelper;

import java.util.Collection;

/**
 * Immutable class representing number inbetween 0 and 1; a percentage.
 */
public final class Percentage {
    private final float value;

    private Percentage(final double value) {
        // We cast to float, as that's more than precise enough for our needs and allows for flexibility with inputs.
        this.value = (float)Math.clamp(value, 0.0, 1.0);
    }

    public static Percentage ZERO = new Percentage(0.0f);
    public static Percentage FULL = new Percentage(1.0f);


    //region Constructors
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

    public static Percentage ofAverage(Collection<Percentage> percentages) {
        if (percentages.isEmpty()) return ZERO;
        double sum = percentages.stream().mapToDouble(Percentage::get).sum();
        return new Percentage(sum / (float)percentages.size());
    }
    //endregion


    //region Math operations
    public Percentage add(final Percentage other) {
        return new Percentage(this.value + other.value);
    }

    public Percentage subtract(final Percentage other) {
        return new Percentage(this.value - other.value);
    }
    //endregion


    //region Getters
    public float get() {
        return value;
    }
    //endregion


    //region Java internals
    @Override
    public String toString() {
        return MathHelper.round(value * 100, 0) + "%";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Percentage other && this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }
    //endregion

}
