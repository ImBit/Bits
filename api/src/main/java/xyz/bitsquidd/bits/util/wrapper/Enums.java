/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.util.wrapper;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Provides static utility methods for searching and resolving {@link Enum} constants.
 *
 * @since 0.0.10
 */
public final class Enums {
    private Enums() {}

    /**
     * Resolves an enum constant by its name, ignoring case sensitivity.
     *
     * @param <T>       the enum type
     * @param enumClass the class of the enum
     * @param name      the name of the constant to find, may be null
     *
     * @return the resolved constant, or null if not found or name is null
     *
     * @since 0.0.10
     */
    public static <T extends Enum<T>> @Nullable T valueOf(Class<T> enumClass, @Nullable String name) {
        return valueOfOrDefault(enumClass, name, null);
    }

    /**
     * Resolves an enum constant by its name (case-insensitive), or returns a default value if not found.
     *
     * @param <T>          the enum type
     * @param enumClass    the class of the enum
     * @param name         the name of the constant to find, may be null
     * @param defaultValue the value to return if resolution fails
     *
     * @return the resolved constant or the default value
     *
     * @since 0.0.10
     */
    public static <T extends Enum<T>> @Nullable T valueOfOrDefault(Class<T> enumClass, @Nullable String name, @Nullable T defaultValue) {
        if (name == null) return defaultValue;

        try {
            return Enum.valueOf(enumClass, name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            return defaultValue;
        }
    }

    /**
     * Searches for an enum constant that matches a specific predicate.
     *
     * @param <T>        the enum type
     * @param enumClass  the class of the enum
     * @param identifier the condition to match against each constant
     *
     * @return an optional containing the first matching constant
     *
     * @since 0.0.10
     */
    public static <T extends Enum<T>> Optional<T> getFromIdentifier(Class<T> enumClass, Predicate<T> identifier) {
        for (T constant : enumClass.getEnumConstants()) {
            if (identifier.test(constant)) {
                return Optional.of(constant);
            }
        }
        return Optional.empty();
    }

}
