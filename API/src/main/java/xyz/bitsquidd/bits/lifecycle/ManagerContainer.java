/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lifecycle;

import xyz.bitsquidd.bits.helper.Safety;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Container for multiple CoreManagers.
 */
public abstract class ManagerContainer implements CoreManager {
    private final Set<CoreManager> managers = new LinkedHashSet<>();

    protected final <T extends CoreManager> T registerManager(T manager) {
        managers.add(manager);
        return manager;
    }

    public final void registerManagers(Iterable<? extends CoreManager> managers) {
        managers.forEach(this::registerManager);
    }


    public final List<CoreManager> getAllManagers() {
        return List.copyOf(managers);
    }


    @Override
    public void startup() {
        for (CoreManager manager : getAllManagers()) {
            startupManager(manager);
        }
    }

    protected void startupManager(CoreManager manager) {
        Safety.safeExecute(manager.getClass().getSimpleName(), manager::startup);
    }

    @Override
    public void initialise() {
        for (CoreManager manager : getAllManagers()) {
            initialiseManager(manager);
        }
    }

    protected void initialiseManager(CoreManager manager) {
        Safety.safeExecute(manager.getClass().getSimpleName(), manager::initialise);
    }

    @Override
    public void cleanup() {
        for (CoreManager manager : getAllManagers()) {
            cleanupManager(manager);
        }
    }

    protected void cleanupManager(CoreManager manager) {
        Safety.safeExecute(manager.getClass().getSimpleName(), manager::cleanup);
    }

    @Override
    public void shutdown() {
        for (CoreManager manager : getAllManagers()) {
            shutdownManager(manager);
        }
    }

    protected void shutdownManager(CoreManager manager) {
        Safety.safeExecute(manager.getClass().getSimpleName(), manager::shutdown);
    }

}
