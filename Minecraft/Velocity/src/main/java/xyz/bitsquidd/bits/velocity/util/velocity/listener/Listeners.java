/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.velocity.util.velocity.listener;

import com.velocitypowered.api.event.EventManager;

import xyz.bitsquidd.bits.velocity.VelocityBitsConfig;

import java.util.Collection;


/**
 * Utility class for managing Velocity event listeners.
 * <p>
 * See <a href="https://docs.papermc.io/velocity/dev/event-api/">Working with events</a> for more information.
 */
public final class Listeners {
    private static final Object plugin = VelocityBitsConfig.get().getPlugin();
    private static final EventManager eventManager = VelocityBitsConfig.get().getServer().getEventManager();

    private Listeners() {}

    public static void register(final Object listener) {
        eventManager.register(plugin, listener);
    }

    public static void unregister(final Object listener) {
        eventManager.unregisterListener(plugin, listener);
    }

    public static void unregister(final Collection<Object> listeners) {
        listeners.forEach(Listeners::unregister);
    }

}
