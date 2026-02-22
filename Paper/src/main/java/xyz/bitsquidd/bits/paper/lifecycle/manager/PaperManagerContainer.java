/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.lifecycle.manager;

import org.bukkit.event.Listener;

import xyz.bitsquidd.bits.lifecycle.manager.CoreManager;
import xyz.bitsquidd.bits.lifecycle.manager.ManagerContainer;
import xyz.bitsquidd.bits.paper.util.bukkit.listener.Listeners;
import xyz.bitsquidd.bits.paper.util.bukkit.listener.PermanentListener;

/**
 * ManagerContainer implementation for Paper, which handles registering and unregistering listeners with the Bukkit event system.
 */
public abstract class PaperManagerContainer extends ManagerContainer {

    @Override
    protected void startupManager(CoreManager manager) {
        if (manager instanceof PermanentListener listener) Listeners.register(listener);
        super.startupManager(manager);
    }

    @Override
    protected void initialiseManager(CoreManager manager) {
        if (manager instanceof Listener listener && !(manager instanceof PermanentListener)) Listeners.register(listener);
        super.initialiseManager(manager);
    }

    @Override
    protected void cleanupManager(CoreManager manager) {
        super.cleanupManager(manager);
        if (manager instanceof Listener listener && !(listener instanceof PermanentListener)) Listeners.unregister(listener);
    }

    @Override
    protected void shutdownManager(CoreManager manager) {
        super.shutdownManager(manager);
        if (manager instanceof PermanentListener listener) Listeners.unregister(listener);
    }

}