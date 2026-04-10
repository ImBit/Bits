/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.location.containable.area;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.paper.location.wrapper.BlockPos;
import xyz.bitsquidd.bits.paper.util.bukkit.listener.Listeners;
import xyz.bitsquidd.bits.paper.util.bukkit.runnable.Runnables;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Wrapper for an {@link Area Area} that detects when players enter or leave it.
 */
public class TickingArea implements Listener {
    protected final Area area;

    private final Set<UUID> playersInside = new HashSet<>();

    private State state = State.TICKING;
    private @Nullable BukkitTask ticker;


    private enum State {
        TICKING,
        PAUSED
    }


    public TickingArea(Area area) {
        this.area = area;
    }


    public final void spawn() {
        Listeners.register(this);
        ticker = Runnables.timer(
          () -> {
              switch (state) {
                  case TICKING -> tick();
                  case PAUSED -> {
                      // Do nothing
                  }
              }
          }, 0L, 1L
        );
        onSpawn();
    }

    protected void onSpawn() {
        // Default empty implementation: Override if needed.
    }

    public void despawn() {
        Listeners.unregister(this);
        ticker = Runnables.cleanup(ticker);
        onDespawn();
    }

    protected void onDespawn() {
        // Default empty implementation: Override if needed.
    }


    public void pause() {
        this.state = State.PAUSED;
    }

    public void resume() {
        this.state = State.TICKING;
    }


    private void tick() {
        Set<UUID> currentPlayers = new HashSet<>();
        area.world().getPlayers().stream().filter(p -> area.contains(BlockPos.of(p))).forEach(p -> currentPlayers.add(p.getUniqueId()));

        Set<UUID> exited = new HashSet<>(playersInside);
        exited.removeAll(currentPlayers);

        Set<UUID> entered = new HashSet<>(currentPlayers);
        entered.removeAll(playersInside);

        playersInside.clear();
        playersInside.addAll(currentPlayers);

        exited.forEach(uuid -> Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(this::onPlayerExit));
        entered.forEach(uuid -> Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(this::onPlayerEnter));

        onTick();
    }

    protected void onTick() {
        // Default empty implementation: Override if needed.
    }


    protected void onPlayerEnter(Player player) {
        // Default empty implementation: Override if needed.
    }

    protected void onPlayerExit(Player player) {
        // Default empty implementation: Override if needed.
    }


    public final void kickOutAllPlayers() {
        playersInside.forEach(uuid -> Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(this::onPlayerExit));
        playersInside.clear();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (playersInside.remove(player.getUniqueId())) {
            onPlayerExit(player);
        }
    }


}
