package xyz.bitsquidd.bits.lib.helper;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * A collection of utilities for working with Enums.
 */
public final class Enums {
    private Enums() {}

    public static <T extends Enum<T>> @Nullable T valueOf(Class<T> enumClass, @Nullable String name) {
        return valueOfOrDefault(enumClass, name, null);
    }

    public static <T extends Enum<T>> @Nullable T valueOfOrDefault(Class<T> enumClass, @Nullable String name, @Nullable T defaultValue) {
        if (name == null) return defaultValue;

        try {
            return Enum.valueOf(enumClass, name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            return defaultValue;
        }
    }

}
