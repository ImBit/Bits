/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lifecycle.manager;

import xyz.bitsquidd.bits.util.Safety;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Containerized storage for multiple {@link CoreManager}s, allowing for easy registration and lifecycle management of multiple managers.
 */
public abstract class ManagerContainer implements CoreManager {
    private final Set<CoreManager> managers = new LinkedHashSet<>();

    protected final <T extends CoreManager> T registerManager(T manager) {
        managers.add(manager);
        return manager;
    }

    protected final void registerManagers(Iterable<? extends CoreManager> managers) {
        managers.forEach(this::registerManager);
    }


    public final List<CoreManager> getAllManagers() {
        return List.copyOf(managers);
    }


    @Override
    public void startup() {
        getAllManagers().forEach(this::startupManager);
    }

    protected void startupManager(CoreManager manager) {
        Safety.safeExecute(manager.getClass().getSimpleName(), manager::startup);
    }

    @Override
    public void initialise() {
        getAllManagers().forEach(this::initialiseManager);
    }

    protected void initialiseManager(CoreManager manager) {
        Safety.safeExecute(manager.getClass().getSimpleName(), manager::initialise);
    }

    @Override
    public void cleanup() {
        getAllManagers().forEach(this::cleanupManager);
    }

    protected void cleanupManager(CoreManager manager) {
        Safety.safeExecute(manager.getClass().getSimpleName(), manager::cleanup);
    }

    @Override
    public void shutdown() {
        getAllManagers().forEach(this::shutdownManager);
    }

    protected void shutdownManager(CoreManager manager) {
        Safety.safeExecute(manager.getClass().getSimpleName(), manager::shutdown);
    }

}
