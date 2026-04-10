/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.inventory;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents the content of a single inventory slot.
 *
 * @since 0.0.13
 */
public record SlotContent(
  int slot,
  @Nullable ItemStack item
) {
    public static Collection<SlotContent> fromItemStackArray(@Nullable ItemStack[] itemStacks) {
        List<SlotContent> slotContents = new ArrayList<>();

        for (int i = 0; i < itemStacks.length; i++) {
            slotContents.add(new SlotContent(i, itemStacks[i]));
        }

        return slotContents;
    }

}
