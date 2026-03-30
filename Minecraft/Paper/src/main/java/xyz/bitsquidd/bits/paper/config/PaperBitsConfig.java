/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.config;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.bitsquidd.bits.config.BitsConfig;
import xyz.bitsquidd.bits.config.MCBitsConfig;
import xyz.bitsquidd.bits.log.Logger;
import xyz.bitsquidd.bits.paper.format.CommonPaperFormatters;
import xyz.bitsquidd.bits.paper.log.PaperBitsLogger;
import xyz.bitsquidd.bits.paper.util.bukkit.runnable.Runnables;
import xyz.bitsquidd.bits.permission.Permission;

import java.util.Locale;

public class PaperBitsConfig extends MCBitsConfig {
    private final JavaPlugin plugin;

    public PaperBitsConfig(JavaPlugin plugin) {
        this.plugin = plugin;

        initialise();
    }

    public static PaperBitsConfig get() {
        return (PaperBitsConfig)BitsConfig.get();
    }


    @Override
    public void startup() {
        super.startup();
        CommonPaperFormatters.init();
    }


    public final JavaPlugin plugin() {
        return plugin;
    }

    @Override
    protected Logger createLogger() {
        return new PaperBitsLogger(plugin, Logger.LogFlags.defaultFlags());
    }

    @Override
    public boolean hasPermission(Audience audience, Permission permission) {
        if (audience instanceof CommandSender commandSender) {
            return commandSender.hasPermission(permission.toString());
        } else {
            return false;
        }
    }

    @Override
    public void registerPermission(Permission permission) {
        Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission(permission.toString()));
    }

    @Override
    public Locale getLocale(Audience audience) {
        checkInitialized();
        if (audience instanceof Player player) {
            return player.locale();
        }
        return Locale.getDefault();
    }

    @Override
    public Audience getAll() {
        return Audience.audience(Bukkit.getOnlinePlayers());
    }

    @Override
    public void runLater(Runnable runnable, long delay) {
        Runnables.later(runnable, delay / 50); // Convert ms to ticks
    }

}
