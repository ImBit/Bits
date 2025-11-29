package xyz.bitsquidd.bits.lib.helper;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
        registerFormatter(Location.class, Formatter::location);
        registerFormatter(Player.class, Formatter::player);
        registerFormatter(Collection.class, Formatter::collection);
        registerFormatter(Map.class, Formatter::map);
        registerFormatter(Block.class, Formatter::block);
        registerFormatter(World.class, Formatter::world);
        registerFormatter(ItemStack.class, Formatter::itemStack);
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


    public static String location(Location location) {
        return location(location, true);
    }

    public static String location(Location location, boolean includeWorld) {
        StringBuilder sb = new StringBuilder();

        if (includeWorld) sb.append(location.getWorld().getName()).append(": ");

        sb.append(String.format("%.1f", location.getX())).append(", ");
        sb.append(String.format("%.1f", location.getY())).append(", ");
        sb.append(String.format("%.1f", location.getZ()));
        return sb.toString();
    }


    public static String player(Player player) {
        return player.getName();
    }


    public static String block(Block block) {
        return block.getType() + "@" + location(block.getLocation(), true);
    }


    public static String world(World world) {
        return world.getName();
    }


    public static String itemStack(ItemStack itemStack) {
        return itemStack.getAmount() + "x " + itemStack.getType();
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
