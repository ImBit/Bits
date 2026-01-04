package xyz.bitsquidd.bits.lib.format;

import org.jspecify.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility class for formatting various objects into readable strings.
 */
public final class Formatter {
    private static final Map<Class<?>, FormatterFunction<?>> formatters = new HashMap<>();

    @FunctionalInterface
    public interface FormatterFunction<T> {
        String apply(T obj);

    }

    public static <T> void registerFormatter(Class<T> clazz, FormatterFunction<? super T> formatter) {
        formatters.put(clazz, formatter);
    }

    @SuppressWarnings("unchecked")
    public static String format(@Nullable Object obj) {
        if (obj == null) return "null";

        for (Map.Entry<Class<?>, FormatterFunction<?>> entry : formatters.entrySet()) {
            if (entry.getKey().isAssignableFrom(obj.getClass())) {
                return ((FormatterFunction<Object>)entry.getValue()).apply(obj);
            }
        }

        return obj.toString();
    }

    public static final FormatterFunction<Date> DATE_FORMATTER = date -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

    public static final FormatterFunction<UUID> UUID_FORMATTER = UUID::toString;

    public static final FormatterFunction<Map<?, ?>> MAP_FORMATTER = map -> {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) sb.append(", ");
            first = false;
            sb.append(format(entry.getKey())).append(": ").append(format(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    };

    public static final FormatterFunction<Collection<?>> COLLECTION_FORMATTER = collection -> {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object item : collection) {
            if (!first) sb.append(", ");
            first = false;
            sb.append(format(item));
        }
        sb.append("]");
        return sb.toString();
    };

    static {
//        registerFormatter(Collection.class, COLLECTION_FORMATTER);
//        registerFormatter(Map.class, MAP_FORMATTER);
        registerFormatter(Date.class, DATE_FORMATTER);
        registerFormatter(UUID.class, UUID_FORMATTER);
    }

}
