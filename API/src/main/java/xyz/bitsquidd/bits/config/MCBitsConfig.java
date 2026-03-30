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

public abstract class MCBitsConfig extends BitsConfig {
    private boolean paused = false;

    protected final BitsCommandManager<?> commandManager;


    public MCBitsConfig() {
        this.commandManager = registerManager(createCommandManager());
    }


    public abstract boolean hasPermission(Audience audience, Permission permission);

    public abstract Locale getLocale(Audience audience);

    public abstract Audience getAll();


    //region Pause API
    public boolean isPaused() {
        return paused;
    }

    public void pause(boolean paused) {
        this.paused = paused;
    }
    //endregion


    //region Command Manager
    protected abstract BitsCommandManager<?> createCommandManager();

    public final BitsCommandManager<?> getCommandManager() {
        return commandManager;
    }
    //endregion


}
