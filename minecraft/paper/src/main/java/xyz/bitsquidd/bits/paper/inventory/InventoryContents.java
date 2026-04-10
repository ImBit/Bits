/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.inventory;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lifecycle.builder.Buildable;
import xyz.bitsquidd.bits.log.Logger;
import xyz.bitsquidd.bits.paper.item.Items;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Mutable representation of an inventory's contents.
 */
public final class InventoryContents implements Iterable<SlotContent> {
    private final Map<Integer, @Nullable ItemStack> inventoryLayout;

    public static InventoryContents empty() {
        return new InventoryContents(new HashMap<>());
    }

    private InventoryContents(Map<Integer, @Nullable ItemStack> inventoryLayout) {
        this.inventoryLayout = inventoryLayout;
    }

    public List<SlotContent> toSlotContents() {
        List<SlotContent> slotContents = new ArrayList<>();

        inventoryLayout.forEach((integer, itemStack) -> slotContents.add(new SlotContent(integer, itemStack)));

        return slotContents;
    }

    public Map<Integer, @Nullable ItemStack> getLayout() {
        return Map.copyOf(inventoryLayout);
    }

    public InventoryContents removeItems(Predicate<SlotContent> removeIf) {
        toSlotContents().forEach(((slotContent) -> {
            if (removeIf.test(slotContent)) {
                inventoryLayout.put(slotContent.slot(), null);
            }
        }));

        return this;
    }

    @Override
    public Iterator<SlotContent> iterator() {
        return toSlotContents().iterator();
    }

    public void put(int slot, @Nullable ItemStack item) {
        inventoryLayout.put(slot, item);
    }

    /**
     * Applies this inventory layout to the given inventory holder, using the provided predicate to determine
     * whether to override existing items.
     * <p>
     * Note: <ul>
     * <li>Null Items will override existing items.
     * <li>If the current item is null, the new item will not always be applied.
     * </ul>
     *
     * @param inventoryHolder The inventory holder to apply the layout to.
     * @param shouldOverride  A BiPredicate that takes <b>Current Item -> New Item</b>, returning true if the new item
     *                        should override the current item.
     */
    public void apply(InventoryHolder inventoryHolder, BiPredicate<SlotContent, SlotContent> shouldOverride) {
        @Nullable ItemStack[] vanillaContents = inventoryHolder.getInventory().getContents();

        for (int i = 0; i < vanillaContents.length; i++) {
            @Nullable ItemStack newItem = inventoryLayout.get(i);
            @Nullable ItemStack currentItem = vanillaContents[i];

            if (shouldOverride.test(new SlotContent(i, currentItem), new SlotContent(i, newItem))) vanillaContents[i] = newItem;
        }

        Inventories.Item.set(inventoryHolder, vanillaContents);
    }

    public void apply(InventoryHolder inventoryHolder) {
        apply(inventoryHolder, (currentSlotContent, newSlotContent) -> true);
    }


    public boolean isEquivalent(@Nullable InventoryContents other) {
        if (other == null) return false;

        // Get all non-null items from both inventories
        List<ItemStack> thisItems = inventoryLayout.values().stream()
          .filter(item -> !Items.isNull(item))
          .toList();

        List<ItemStack> otherItems = other.inventoryLayout.values().stream()
          .filter(item -> !Items.isNull(item))
          .toList();

        List<ItemStack> remainingOtherItems = new ArrayList<>(otherItems);

        for (ItemStack thisItem : thisItems) {
            int thisAmount = thisItem.getAmount();
            int matchedAmount = 0;

            Iterator<ItemStack> iterator = remainingOtherItems.iterator();
            while (iterator.hasNext() && matchedAmount < thisAmount) {
                ItemStack otherItem = iterator.next();

                if (Items.isSimilar(thisItem, otherItem)) {
                    int availableAmount = otherItem.getAmount();
                    int takeAmount = Math.min(thisAmount - matchedAmount, availableAmount);

                    matchedAmount += takeAmount;

                    if (takeAmount == availableAmount) {
                        iterator.remove();
                    } else {
                        otherItem.setAmount(availableAmount - takeAmount);
                    }
                }
            }

            // If we couldn't match the full amount, inventories are not equivalent
            if (matchedAmount != thisAmount) return false;
        }

        return remainingOtherItems.isEmpty();
    }

    public int getSize() {
        // Note: If there are missing definitions between min -> max, this will be inaccurate. This will be fine for all cases where we define .of(InventoryHolder) as this populates with nulls.
        return inventoryLayout.size();
    }

    public boolean hasItem(int slot) {
        ItemStack item = inventoryLayout.get(slot);
        return !Items.isNull(item);
    }

    public boolean isValidSlot(int slot) {
        return slot >= 0 && slot < getSize();
    }

    public Optional<ItemStack> get(int slot) {
        return Optional.ofNullable(inventoryLayout.get(slot));
    }

    public InventoryContents set(int slot, @Nullable ItemStack item) {
        if (!isValidSlot(slot)) Logger.warn("Attempted to set item in slot " + slot + " which is not in the inventory layout.");

        inventoryLayout.put(slot, item);
        return this;
    }

    public InventoryContents add(@Nullable ItemStack item) {
        for (int i = 0; isValidSlot(i); i++) {
            if (get(i).isEmpty()) {
                inventoryLayout.put(i, item);
                return this;
            }
        }

        Logger.warn("Attempted to add item to full inventory layout.");
        return this;
    }

    public InventoryContents setOrAdd(int slot, @Nullable ItemStack item) {
        Logger.info(slot + " " + isValidSlot(slot) + ":" + hasItem(slot));

        if (isValidSlot(slot) && !hasItem(slot)) {
            set(slot, item);
        } else {
            add(item);
        }
        return this;
    }

    public InventoryContents merge(InventoryContents other) {
        this.inventoryLayout.putAll(other.inventoryLayout);
        return this;
    }


    //region Builder
    public static InventoryContents of(InventoryHolder inventoryHolder) {
        return builder(inventoryHolder.getInventory()).build();
    }

    public static InventoryContents of(Inventory inventory) {
        return builder(inventory).build();
    }


    public static Builder builder(InventoryHolder inventoryHolder) {
        return builder(inventoryHolder.getInventory());
    }

    public static Builder builder(Inventory inventory) {
        Builder builder = new Builder(inventory.getSize());

        @Nullable ItemStack[] vanillaContents = inventory.getContents();
        for (int i = 0; i < vanillaContents.length; i++) {
            builder.addItem(i, vanillaContents[i]);
        }

        return builder;
    }

    public static Builder builder() {
        return new Builder(41); // Default to a size of 41, which is the size of a player's inventory (including armor and offhand).
    }

    public static final class Builder implements Buildable<InventoryContents> {
        private final Map<Integer, @Nullable ItemStack> layout = new HashMap<>();

        private Builder(int maxSize) {
            for (int i = 0; i < maxSize; i++) {
                layout.put(i, null);
            }
        }

        public Builder addItem(int slot, @Nullable ItemStack item) {
            layout.put(slot, item);

            // As we are using the builder, we expect to
            if (slot >= layout.size()) {
                for (int i = layout.size(); i <= slot; i++) {
                    layout.put(i, null);
                }
            }

            return this;
        }

        public Builder addItem(EquipmentSlot slot, @Nullable ItemStack item) {
            return addItem(Inventories.Slot.from(slot), item);
        }

        @Override
        public InventoryContents build() {
            return new InventoryContents(layout);
        }

    }
    //endregion

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        inventoryLayout.forEach((slot, item) -> {
            string.append(slot).append(": ").append(Items.isNull(item) ? "null" : item.getType()).append(" | ");
        });

        return string.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof InventoryContents other)) return false;
        return Objects.equals(this.inventoryLayout, other.inventoryLayout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryLayout);
    }

}
