/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.util.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.bitsquidd.bits.paper.PaperBitsConfig;

import java.util.Collection;
import java.util.function.Predicate;


public final class Listeners {
    private static final JavaPlugin plugin = PaperBitsConfig.get().plugin();

    private Listeners() {}

    public static void register(final Listener listener) {
        if (!isRegistered(listener)) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    public static void unregister(final Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public static void unregister(final Collection<Listener> listeners) {
        listeners.forEach(Listeners::unregister);
    }

    public static void unregister(final Predicate<Listener> filter) {
        HandlerList.getRegisteredListeners(plugin).stream()
          .map(RegisteredListener::getListener)
          .filter(filter)
          .forEach(Listeners::unregister);
    }


    public static boolean isRegistered(final Listener listener) {
        return HandlerList.getRegisteredListeners(plugin).stream()
          .map(RegisteredListener::getListener)
          .anyMatch(l -> l == listener);
    }

}
