package xyz.bitsquidd.bits.lib.helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnumHelper {
    public static <T extends Enum<T>> @Nullable T valueOf(@NotNull Class<T> enumClass, @Nullable String name, @Nullable T defaultValue) {
        if (name == null) return defaultValue;

        try {
            return Enum.valueOf(enumClass, name.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return defaultValue;
        }
    }

}
