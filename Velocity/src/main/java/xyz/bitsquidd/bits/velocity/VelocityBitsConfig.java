/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.audience.Audience;

import xyz.bitsquidd.bits.BitsConfig;
import xyz.bitsquidd.bits.log.Logger;
import xyz.bitsquidd.bits.permission.Permission;
import xyz.bitsquidd.bits.velocity.log.VelocityBitsLogger;

import java.util.Locale;

public class VelocityBitsConfig extends BitsConfig {
    private final Object plugin;
    private final ProxyServer server;
    private final org.slf4j.Logger slf4j;

    public VelocityBitsConfig(ProxyServer server, Object plugin, org.slf4j.Logger slf4j) {
        this.server = server;
        this.plugin = plugin;
        this.slf4j = slf4j;
    }

    public static VelocityBitsConfig get() {
        return (VelocityBitsConfig)BitsConfig.get();
    }


    public Object getPlugin() {
        checkInitialized();
        return plugin;
    }

    public ProxyServer getServer() {
        checkInitialized();
        return server;
    }

    @Override
    protected Logger createLogger() {
        return new VelocityBitsLogger(slf4j, Logger.LogFlags.defaultFlags());
    }

    @Override
    public boolean hasPermission(Audience audience, Permission permission) {
        checkInitialized();
        if (audience instanceof CommandSource commandSource) {
            return commandSource.hasPermission(permission.toString());
        } else {
            return false;
        }
    }

    @Override
    public void registerPermission(Permission permission) {
        // Unimplemented in Velocity
    }

    @Override
    public Locale getLocale(Audience audience) {
        checkInitialized();
        if (audience instanceof Player player) {
            Locale locale = player.getEffectiveLocale();
            if (locale != null) player.getEffectiveLocale();
        }
        return Locale.getDefault();
    }

    @Override
    public Audience getAll() {
        return Audience.audience(getServer().getAllPlayers());
    }

}
