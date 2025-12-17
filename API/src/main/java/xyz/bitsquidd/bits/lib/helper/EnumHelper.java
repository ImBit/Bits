package xyz.bitsquidd.bits.lib.helper;

import org.jetbrains.annotations.Nullable;

public class EnumHelper {

    public static <T extends Enum<T>> @Nullable T valueOf(Class<T> enumClass, @Nullable String name) {
        return valueOfOrDefault(enumClass, name, null);
    }

    public static <T extends Enum<T>> @Nullable T valueOfOrDefault(Class<T> enumClass, @Nullable String name, @Nullable T defaultValue) {
        if (name == null) return defaultValue;

        try {
            return Enum.valueOf(enumClass, name.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return defaultValue;
        }
    }

}
