/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.config;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.permission.Permission;

import java.util.Locale;

public class FabricServerBitsConfig extends FabricBitsConfig {
    private @Nullable MinecraftServer server;
    private @Nullable FabricServerAudiences adventure;

    public FabricServerBitsConfig(org.slf4j.Logger slf4j) {
        super(slf4j);
    }

    public static FabricServerBitsConfig get() {
        return (FabricServerBitsConfig)BitsConfig.get();
    }

    @Override
    public void startup() {
        super.startup();
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            this.server = server;
            this.adventure = FabricServerAudiences.of(server);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            this.server = null;
            this.adventure = null;
            pendingTasks.clear();
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> tickAll());
    }

    @Override
    public Audience getAll() {
        if (server == null || adventure == null) {
            return Audience.empty();
        }

        return adventure.audience(server.getPlayerManager().getPlayerList());
    }

    @Override
    public Locale getLocale(Audience audience) {
        if (audience instanceof ServerPlayerEntity player) return player.getLocale();     // returns the client-reported Locale
        return Locale.getDefault();
    }

    @Override
    public void registerPermission(Permission permission) {
        // TODO
    }

    @Override
    public boolean hasPermission(Audience audience, Permission permission) {
        if (audience instanceof ServerPlayerEntity player) return Permissions.check(player, permission.getKey());
        return true;
    }

}