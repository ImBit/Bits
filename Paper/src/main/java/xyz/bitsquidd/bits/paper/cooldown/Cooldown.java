/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.cooldown;

import net.kyori.adventure.key.Key;

import java.time.Duration;
import java.util.UUID;

// TODO - duration cooldowns might be better async?
public record Cooldown(
  Key key,
  long ticks
) {
    private static final CooldownManager COOLDOWN_MANAGER = CooldownManager.INSTANCE;

    public static Cooldown of(Key key, long ticks) {
        return new Cooldown(key, ticks);
    }

    public static Cooldown ofDuration(Key key, Duration duration) {
        return new Cooldown(key, CooldownManager.toTicks(duration));
    }


    public void apply(UUID uuid) {
        COOLDOWN_MANAGER.addCooldown(uuid, this);
    }

    public void remove(UUID uuid) {
        remove(uuid, false);
    }

    public void remove(UUID uuid, boolean ignoreExpire) {
        COOLDOWN_MANAGER.removeCooldown(uuid, this, ignoreExpire);
    }


    public boolean has(UUID uuid) {
        return COOLDOWN_MANAGER.isOnCooldown(uuid, this);
    }

    public long remainingTicks(UUID uuid) {
        return COOLDOWN_MANAGER.getRemainingTicks(uuid, this);
    }


    /**
     * Runs the action if the cooldown is not ready (on cooldown).
     */
    public Cooldown ifNotReady(UUID uuid, Runnable action) {
        if (has(uuid)) action.run();
        return this;
    }

    /**
     * Runs the action if the cooldown is ready (not on cooldown).
     */
    public Cooldown ifReady(UUID uuid, Runnable action) {
        if (!has(uuid)) action.run();
        return this;
    }

    /**
     * Applies the cooldown and runs the action if the cooldown is ready (not on cooldown).
     */
    public Cooldown applyIfReady(UUID uuid, Runnable action) {
        ifReady(
          uuid, () -> {
              apply(uuid);
              action.run();
          }
        );
        return this;
    }


    //region Java Overrides
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Cooldown other) && this.key.equals(other.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return "Cooldown:" + key;
    }
    //endregion

}
