/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for manipulating ItemStacks.
 *
 * @since 0.0.13
 */
public final class Items {
    private Items() {}

    /**
     * Determines if an ItemStack is null or null-like.
     */
    public static boolean isNull(final @Nullable ItemStack itemStack) {
        return itemStack == null || itemStack.getType().isAir() || itemStack.getAmount() <= 0;
    }

    public static boolean isSimilar(final @Nullable ItemStack itemStack1, final @Nullable ItemStack itemStack2) {
        if (isNull(itemStack1) || isNull(itemStack2)) return false;
        return itemStack1.isSimilar(itemStack2);
    }

}
