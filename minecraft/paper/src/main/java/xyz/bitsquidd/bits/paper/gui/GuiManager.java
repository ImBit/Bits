/*
 * This file is part of the BiomeBattle project.
 *
 * Copyright (c) 2023-2026 BitSquidd - All Rights Reserved
 *
 * Unauthorized copying, distribution, or disclosure of this file, via any medium, is strictly prohibited.
 * Proprietary and confidential - for internal use only.
 */

package xyz.bitsquidd.bits.paper.gui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;

import xyz.bitsquidd.bits.lifecycle.manager.CoreManager;
import xyz.bitsquidd.bits.log.Logger;
import xyz.bitsquidd.bits.paper.gui.slot.GuiSlot;
import xyz.bitsquidd.bits.paper.gui.slot.SlotType;
import xyz.bitsquidd.bits.paper.util.bukkit.listener.PermanentListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class GuiManager implements CoreManager, PermanentListener {
    // The mapping of literal inventory to window.
    // It is important to note:
    //  - Multiple players can be viewing the same GUI.
    //  - A GUI may associate with multiple inventories.
    //  - A single window can only be associated with one inventory AND vice versa.
    private final BiMap<Inventory, Window> openInventories = HashBiMap.create();

    // When a player opens a new window, we push it onto their stack.
    // This allows for backtracking functionality.
    private final Map<UUID, Deque<Gui>> playerGuis = new ConcurrentHashMap<>();


    //region Lifecycle
    //region Window <-> Inventory
    @ApiStatus.Internal
    public void registerInventoryInternal(Window window, Inventory inventory) {
        openInventories.put(inventory, window);
    }

    @ApiStatus.Internal
    public void unregisterInventoryInternal(Inventory inventory) {
        openInventories.remove(inventory);
    }
    //endregion

    //region GUI History
    @ApiStatus.Internal
    public void pushGuiInternal(Player player, Gui gui) {
        Deque<Gui> stack = playerGuis.computeIfAbsent(player.getUniqueId(), k -> new ArrayDeque<>());
        stack.push(gui);
    }

    @ApiStatus.Internal
    public void popGuiInternal(Player player, Gui gui) {
        Deque<Gui> stack = playerGuis.get(player.getUniqueId());
        if (stack != null) {
            stack.remove(gui);
            if (stack.isEmpty()) playerGuis.remove(player.getUniqueId());
        }
    }
    //endregion

    public void cleanupStack(Player player) {
        playerGuis.remove(player.getUniqueId());
    }
    //endregion


    //region Listeners
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        Window window = openInventories.get(inventory);
        if (window == null) return;

        window.onClose();

        if (event.getReason() != InventoryCloseEvent.Reason.OPEN_NEW) {
            // If we get here, we know that the player wants their inventories closed.
            // We therefore cleanup the entire stack, restoring items in order of most recent to least recent.
            cleanupStack(event.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        getOpenWindow(event.getWhoClicked()).ifPresent(window -> {
            boolean isTop = Objects.equals(event.getView().getTopInventory(), event.getClickedInventory());
            window.onClick(event.getCurrentItem(), GuiSlot.of(isTop ? SlotType.GUI : SlotType.PLAYER, event.getSlot()), event.getClick());
        });
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        cleanupStack(event.getPlayer());
    }
    //endregion


    //region Getters
    public Optional<Window> getOpenWindow(Player player) {
        return Optional.ofNullable(openInventories.get(player.getEntity().getOpenInventory().getTopInventory()));
    }

    public void backtrack(Player player) {
        Deque<Gui> stack = playerGuis.get(player.getUniqueId());
        Logger.info("stack: " + stack);
        if (stack != null && !stack.isEmpty()) {
            Logger.info("closing old!");
            stack.pop();

            if (!stack.isEmpty()) {
                Gui previous = stack.pop(); // We pop as itll get re-added again
                Logger.info("opening previous window: " + previous);
                previous.addViewer(player);
            }
        }
    }

    public Set<Window> getAllWindows(Predicate<Window> filter) {
        return openInventories.values()
          .stream()
          .filter(filter)
          .collect(Collectors.toSet());
    }

    public Set<Window> getOpenWindows(Predicate<Window> filter) {
        return openInventories.values().stream().filter(filter).collect(Collectors.toSet());
    }
    //endregion

}
