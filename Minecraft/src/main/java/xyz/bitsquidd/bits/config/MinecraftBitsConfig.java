/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.config;

import net.kyori.adventure.audience.Audience;

import xyz.bitsquidd.bits.command.BitsCommandManager;
import xyz.bitsquidd.bits.permission.Permission;

import java.util.Locale;

public abstract class MinecraftBitsConfig extends BitsConfig {
    private boolean paused = false;

    protected BitsCommandManager<?> commandManager;


    @Override
    public void startup() {
        this.commandManager = registerManager(createCommandManager());
        
        super.startup();
    }

    public static MinecraftBitsConfig get() {
        return (MinecraftBitsConfig)BitsConfig.get();
    }


    //region Sendable Utils
    public abstract boolean hasPermission(Audience audience, Permission permission);

    public abstract void registerPermission(Permission permission);

    public abstract Locale getLocale(Audience audience);

    public abstract Audience getAll();
    //endregion


    //region Runnable API
    public boolean isPaused() {
        return paused;
    }

    public void pause(boolean paused) {
        this.paused = paused;
    }
    //endregion


    //region Managers
    protected abstract BitsCommandManager<?> createCommandManager();

    public BitsCommandManager<?> getCommandManager() {
        return commandManager;
    }
    //endregion


}
