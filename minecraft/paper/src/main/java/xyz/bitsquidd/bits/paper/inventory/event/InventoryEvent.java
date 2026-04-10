/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.inventory.event;

import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

/**
 * Custom event for inventory-related actions.
 * Note: Not the same as Bukkit's {@link org.bukkit.event.inventory.InventoryEvent InventoryEvent}
 * (with the same name) as this requires an {@link org.bukkit.inventory.InventoryHolder InventoryHolder}
 * as opposed to an {@link org.bukkit.inventory.InventoryView InventoryView}.
 */
public abstract class InventoryEvent extends Event {
    private final Inventory inventory;

    public InventoryEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public final Inventory getInventory() {
        return inventory;
    }

}
