package xyz.bitsquidd.bits.lib.sendable;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.permission.Permission;

import java.util.Collection;
import java.util.function.Predicate;

@NullMarked
public interface Sendable {

    <T extends Audience> void send(T target);

    @SuppressWarnings("unchecked")
    default <T extends Audience> void sendAll(Predicate<T> predicate) {
        Bukkit.getOnlinePlayers().stream()
              .filter(t -> predicate.test((T)(t)))
              .forEach(this::send);
    }

    default <T extends Audience> void sendAll(Permission permission) {
        sendAll(permission::hasPermission);
    }

    default <T extends Audience> void sendAll(Collection<T> targets) {
        targets.forEach(this::send);
    }
}
