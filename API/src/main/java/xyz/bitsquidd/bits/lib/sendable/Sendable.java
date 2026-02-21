/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.sendable;

import net.kyori.adventure.audience.Audience;

import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.lib.permission.Permission;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Represents an object that can be sent to one or more audiences.
 */
public interface Sendable {

    void send(Audience target);

    default void sendAll(Predicate<Audience> predicate) {
        BitsConfig.get().getAll().forEachAudience(audience -> {
            if (predicate.test(audience)) send(audience);
        });
    }

    default void sendAll(Permission permission) {
        sendAll(permission::hasPermission);
    }

    default void sendAll(Collection<? extends Audience> targets) {
        targets.forEach(this::send);
    }

}
