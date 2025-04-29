package xyz.bitsquidd.bits.lib.helper;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

public class EntityHelper {

    public static <T extends Entity> @Nullable T getNearestEntity(Location location, Class<T> entityClass) {
        return getNearestEntity(location, entityClass, entity -> true);
    }

    public static <T extends Entity> @Nullable T getNearestEntity(Location location, Class<T> entityClass, Predicate<T> filter) {
        if (location == null || location.getWorld() == null) {
            return null;
        }

        Collection<T> entities = location.getWorld().getEntitiesByClass(entityClass);

        return entities.stream()
                .filter(filter)
                .min(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(location)))
                .orElse(null);
    }
}