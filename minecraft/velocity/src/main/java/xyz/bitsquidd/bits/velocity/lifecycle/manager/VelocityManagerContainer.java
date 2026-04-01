/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.velocity.lifecycle.manager;

import xyz.bitsquidd.bits.lifecycle.manager.CoreManager;
import xyz.bitsquidd.bits.lifecycle.manager.ManagerContainer;
import xyz.bitsquidd.bits.velocity.util.velocity.listener.Listener;
import xyz.bitsquidd.bits.velocity.util.velocity.listener.Listeners;
import xyz.bitsquidd.bits.velocity.util.velocity.listener.PermanentListener;


/**
 * ManagerContainer implementation for Velocity, which handles registering and unregistering listeners with the Velocity task system.
 * <p>
 * Developer Note: all objects can act as listeners in Velocity, the {@link Listener listener} annotation is simply for convenience for automatic registration and unregistration here.
 */
public abstract class VelocityManagerContainer extends ManagerContainer {

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