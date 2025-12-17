package xyz.bitsquidd.bits.lib.helper;

import org.jspecify.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Utility class for formatting various objects into readable strings.
 */
public final class Formatter {
    private static final Map<Class<?>, Function<Object, String>> formatters = new HashMap<>();

    static {
        registerFormatter(Collection.class, Formatter::collection);
        registerFormatter(Map.class, Formatter::map);
        registerFormatter(Date.class, Formatter::date);
        registerFormatter(UUID.class, Formatter::uuid);
    }

    public static <T> void registerFormatter(Class<T> clazz, Function<T, String> formatter) {
        formatters.put(clazz, obj -> formatter.apply(clazz.cast(obj)));
    }

    public static String format(@Nullable Object obj) {
        if (obj == null) return "null";
        AtomicReference<String> formatted = new AtomicReference<>(obj.toString());
        formatters.keySet().stream()
              .filter(clazz -> clazz.isAssignableFrom(obj.getClass())).findFirst().ifPresent(formatterClass -> {
                  formatted.set(formatters.get(formatterClass).apply(obj));
              });

        return formatted.get();
    }


    public static String date(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }


    public static String uuid(UUID uuid) {
        return uuid.toString();
    }


    public static String map(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) sb.append(", ");
            first = false;
            sb.append(format(entry.getKey())).append(": ").append(format(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    public static <T> String collection(Collection<T> collection) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (T item : collection) {
            if (!first) sb.append(", ");
            first = false;
            sb.append(format(item));
        }
        sb.append("]");
        return sb.toString();
    }

}
