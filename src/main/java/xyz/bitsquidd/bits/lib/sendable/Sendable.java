package xyz.bitsquidd.bits.lib.sendable;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.permission.Permission;

import java.util.Collection;
import java.util.function.Predicate;

public interface Sendable {

    <T extends CommandSender> void send(@NotNull T target);

    @SuppressWarnings("unchecked")
    default <T extends CommandSender> void sendAll(@NotNull Predicate<T> predicate) {
        Bukkit.getOnlinePlayers().stream()
                .filter(t -> predicate.test((T)(t)))
                .forEach(this::send);
    }

    default <T extends CommandSender> void sendAll(@NotNull Permission permission) {
        sendAll(permission::hasPermission);
    }

    default <T extends CommandSender> void sendAll(@NotNull Collection<T> targets) {
        targets.forEach(this::send);
    }
}
