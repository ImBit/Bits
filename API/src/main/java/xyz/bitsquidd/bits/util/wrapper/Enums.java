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
 * A collection of utilities for working with Enums.
 */
public final class Enums {
    private Enums() {}

    /**
     * Returns the enum constant of the specified enum type with the specified name, ignoring case.
     */
    public static <T extends Enum<T>> @Nullable T valueOf(Class<T> enumClass, @Nullable String name) {
        return valueOfOrDefault(enumClass, name, null);
    }

    /**
     * Returns the enum constant of the specified enum type with the specified name, ignoring case. If no such constant exists, returns the provided default value.
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
     * Returns the enum constant of the specified enum that matches the provided identifier predicate.
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
