/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.paper.inventory.event.InventoryAddItemEvent;
import xyz.bitsquidd.bits.paper.inventory.event.InventorySetItemEvent;
import xyz.bitsquidd.bits.paper.item.Items;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Helper class for inventory-related constants and methods.
 */
public final class Inventories {
    private Inventories() {}

    public static List<ItemStack> getItemStacks(InventoryHolder holder, Predicate<ItemStack> filter) {
        return Arrays.stream(holder.getInventory().getContents())
          .filter(itemStack -> !Items.isNull(itemStack) && filter.test(itemStack))
          .toList();
    }

    public static void forEachItemStack(InventoryHolder player, Consumer<ItemStack> action) {
        Arrays.stream(player.getInventory().getContents())
          .filter(itemStack -> !Items.isNull(itemStack))
          .forEach(action);
    }

    public static final class Item {
        private Item() {}

        //region Adding
        public static Map<Integer, ItemStack> add(final InventoryHolder holder, final @Nullable ItemStack itemStack) {
            return add(holder.getInventory(), itemStack);
        }

        public static Map<Integer, ItemStack> add(final Inventory inventory, final @Nullable ItemStack itemStack) {
            if (Items.isNull(itemStack)) return new HashMap<>();

            if (new InventoryAddItemEvent(inventory, itemStack).callEvent()) {
                Map<Integer, ItemStack> result = inventory.addItem(itemStack);
                if (inventory.getHolder() instanceof Player player) player.updateInventory();
                return result;
            } else {
                return new HashMap<>();
            }
        }
        //endregion


        //region Setting
        public static void set(final InventoryHolder holder, final @Nullable ItemStack[] content) {
            set(holder.getInventory(), content);
        }

        public static void set(final Inventory inventory, final @Nullable ItemStack[] content) {
            if (new InventorySetItemEvent(inventory, SlotContent.fromItemStackArray(content)).callEvent()) {
                inventory.setContents(content);
                if (inventory.getHolder() instanceof Player player) player.updateInventory();
            }
        }

        public static void set(final InventoryHolder holder, final @Nullable ItemStack itemStack, final int slot) {
            set(holder.getInventory(), itemStack, slot);
        }

        public static void set(final Inventory inventory, final @Nullable ItemStack itemStack, final int slot) {
            if (!Slot.isValid(slot, inventory)) return;

            if (new InventorySetItemEvent(inventory, Collections.singleton(new SlotContent(slot, itemStack))).callEvent()) {
                inventory.setItem(slot, itemStack);
                if (inventory.getHolder() instanceof Player player) player.updateInventory();
            }
        }

        public static void set(final InventoryHolder holder, final @Nullable ItemStack itemStack, final EquipmentSlot slot) {
            set(holder.getInventory(), itemStack, slot);
        }

        public static void set(final Inventory inventory, final @Nullable ItemStack itemStack, final EquipmentSlot slot) {
            if (!Slot.isValid(slot, inventory)) return;

            if (new InventorySetItemEvent(inventory, Collections.singleton(new SlotContent(Slot.from(slot), itemStack))).callEvent()) {
                if (inventory instanceof PlayerInventory playerInventory) {
                    playerInventory.setItem(slot, itemStack);
                    if (inventory.getHolder() instanceof Player player) player.updateInventory();
                }
            }
        }
        //endregion


        public static void replace(final InventoryHolder holder, final Function<SlotContent, @Nullable ItemStack> replacement) {
            replace(holder.getInventory(), replacement);
            if (holder instanceof Player player) player.updateInventory();
        }

        public static void replace(final Inventory inventory, final Function<SlotContent, ItemStack> replacement) {
            @Nullable ItemStack[] contents = inventory.getContents();

            for (int i = 0; i < contents.length; i++) {
                ItemStack currentItem = contents[i];
                ItemStack newItem = replacement.apply(new SlotContent(i, currentItem));
                if (newItem != currentItem) inventory.setItem(i, newItem);
            }
        }


        public static void remove(final InventoryHolder holder, final Predicate<SlotContent> removeIf) {
            remove(holder.getInventory(), removeIf);
            if (holder instanceof Player player) player.updateInventory();
        }

        public static void remove(final Inventory inventory, final Predicate<SlotContent> removeIf) {
            @Nullable ItemStack[] contents = inventory.getContents();

            for (int i = 0; i < contents.length; i++) {
                ItemStack currentItem = contents[i];
                if (removeIf.test(new SlotContent(i, currentItem))) {
                    inventory.setItem(i, null);
                }
            }
        }


        public static void clear(final InventoryHolder holder) {
            clear(holder, (slotContent) -> true);
        }

        public static void clear(final InventoryHolder holder, final Predicate<SlotContent> removeIf) {
            clear(holder.getInventory(), removeIf);

            if (holder instanceof Player player) {
                if (removeIf.test(new SlotContent(-1, player.getItemOnCursor()))) player.setItemOnCursor(null);
                if (removeIf.test(new SlotContent(-1, player.getActiveItem()))) player.clearActiveItem();
                player.updateInventory();
            }
        }

        public static void clear(final Inventory inventory) {
            clear(inventory, (slotContent) -> true);
        }

        public static void clear(final Inventory inventory, final Predicate<SlotContent> removeIf) {
            remove(inventory, removeIf);
        }

        public static void addWithOffhand(final Player player, final ItemStack itemStack) {
            if (Items.isNull(itemStack)) return;

            int totalAdded = 0;
            ItemStack offhandItem = player.getInventory().getItemInOffHand();

            if (!Items.isNull(offhandItem)) {
                if (Items.isSimilar(itemStack, offhandItem)) {
                    int maxOffhandSpace = offhandItem.getMaxStackSize() - offhandItem.getAmount();
                    int toAddToOffhand = Math.min(maxOffhandSpace, itemStack.getAmount());
                    offhandItem.setAmount(offhandItem.getAmount() + toAddToOffhand);
                    totalAdded += toAddToOffhand;
                }
            }

            if (itemStack.getAmount() > totalAdded) {
                itemStack.setAmount(itemStack.getAmount() - totalAdded);
                add(player, itemStack);
            }

            player.updateInventory();
        }

        public static void addWithPreferredSlot(final Player player, final ItemStack itemStack, final int slot, final boolean stackWithSimilar) {
            if (stackWithSimilar) {
                InventoryContents contents = InventoryContents.of(player.getInventory());
                int toAdd = itemStack.getAmount();

                for (SlotContent slotContent : contents.toSlotContents()) {
                    ItemStack invItem = slotContent.item();
                    if (invItem != null && Items.isSimilar(itemStack, invItem) && invItem.getAmount() < invItem.getMaxStackSize()) {
                        int space = invItem.getMaxStackSize() - invItem.getAmount();
                        int addAmount = Math.min(space, toAdd);
                        invItem.setAmount(invItem.getAmount() + addAmount);
                        toAdd -= addAmount;
                        if (toAdd <= 0) break;
                    }
                }

                itemStack.setAmount(toAdd);
                if (itemStack.getAmount() <= 0) {
                    player.updateInventory();
                    return;
                }
            }

            if (!Slot.isValid(slot, player.getInventory()) || player.getInventory().getItem(slot) != null) {
                addWithOffhand(player, itemStack);
                return;
            }

            Item.set(player, itemStack, slot);
            player.updateInventory();
        }

    }


    public static final class Slot {
        private Slot() {}

        public static final Set<Integer> ARMOR = Set.of(
          from(EquipmentSlot.HEAD, null),
          from(EquipmentSlot.CHEST, null),
          from(EquipmentSlot.LEGS, null),
          from(EquipmentSlot.FEET, null)
        );

        public static final Integer OFFHAND = from(EquipmentSlot.OFF_HAND);


        public static int from(final EquipmentSlot equipmentSlot) {
            return from(equipmentSlot, null);
        }

        public static int from(final EquipmentSlot equipmentSlot, final @Nullable InventoryHolder holder) {
            // TODO this is not perfect, these slot numbers are specific to the player inventory.
            return switch (equipmentSlot) {
                case HAND -> (holder instanceof Player player) ? player.getInventory().getHeldItemSlot() : -1;
                case OFF_HAND -> 40;
                case FEET -> 36;
                case LEGS -> 37;
                case CHEST -> 38;
                case HEAD -> 39;
                default -> -1;
            };
        }

        public static boolean isValid(final int slot, final Inventory inventory) {
            return slot >= 0 && slot < inventory.getSize();
        }

        public static boolean isValid(final EquipmentSlot equipmentSlot, final Inventory inventory) {
            return isValid(from(equipmentSlot), inventory);
        }

    }

}
