/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.inventory.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when an item is added to an inventory using {@link xyz.bitsquidd.bits.paper.inventory.Inventories.Item#add(Inventory, ItemStack)}.
 * <p>
 * Note that the {@code ItemStack} is mutable, any operations performed on it will affect the item about to be added to the inventory.
 */
public final class InventoryAddItemEvent extends InventoryEvent implements Cancellable {
    public static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private final ItemStack itemStack;

    public InventoryAddItemEvent(Inventory inventory, ItemStack itemStack) {
        super(inventory);
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
