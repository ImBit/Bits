/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.fabric;

import net.kyori.adventure.audience.Audience;

import xyz.bitsquidd.bits.BitsConfig;
import xyz.bitsquidd.bits.fabric.log.FabricBitsLogger;
import xyz.bitsquidd.bits.log.Logger;
import xyz.bitsquidd.bits.permission.Permission;

import java.util.Locale;

public class FabricBitsConfig extends BitsConfig {
    private final org.slf4j.Logger slf4j;

    public FabricBitsConfig(org.slf4j.Logger slf4j) {
        this.slf4j = slf4j;
    }


    public static FabricBitsConfig get() {
        return (FabricBitsConfig)BitsConfig.get();
    }


    @Override
    protected Logger createLogger() {
        return new FabricBitsLogger(slf4j, Logger.LogFlags.defaultFlags());
    }

    @Override
    public void registerPermission(Permission permission) {
        // Unimplemented in Fabric
    }

    @Override
    public boolean hasPermission(Audience audience, Permission permission) {
        return true;
    }

    @Override
    public Locale getLocale(Audience audience) {
        return null;
    }

    @Override
    public Audience getAll() {
        return null;
    }

    @Override
    public void runLater(Runnable runnable, long delay) {

    }

}
