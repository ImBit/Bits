/*
 * This file is part of the BiomeBattle project.
 *
 * Copyright (c) 2023-2026 BitSquidd - All Rights Reserved
 *
 * Unauthorized copying, distribution, or disclosure of this file, via any medium, is strictly prohibited.
 * Proprietary and confidential - for internal use only.
 */

package xyz.bitsquidd.bits.paper.gui.type;

import net.kyori.adventure.text.Component;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Range;

import xyz.bitsquidd.bits.mc.sendable.text.Offset;

public final class ChestGuiType extends GuiType {
    public static final int ROW_HEIGHT = 18;

    public final int rows;

    @SuppressWarnings("ConstantValue")
    ChestGuiType(@Range(from = 1, to = 6) int rows) {
        if (rows < 1 || rows > 6) throw new IllegalArgumentException("ChestGuiType rows must be between [1,6], got: " + rows);
        this.rows = rows;
    }

    @Override
    public MenuType<?> menuType() {
        return switch (rows) {
            case 1 -> MenuType.GENERIC_9x1;
            case 2 -> MenuType.GENERIC_9x2;
            case 3 -> MenuType.GENERIC_9x3;
            case 4 -> MenuType.GENERIC_9x4;
            case 5 -> MenuType.GENERIC_9x5;
            case 6 -> MenuType.GENERIC_9x6;
            default -> throw new IllegalStateException("Unexpected value: " + rows);
        };
    }

    @Override
    public int size() {
        return rows * 9;
    }

    @Override
    public Offset getTopPosition(int slot) {
        int row = slot / 9;
        int col = slot % 9;

        return Offset.of(col * ROW_HEIGHT, 4 + (row * 18));
    }

    @Override
    public int height() {
        return (rows * ROW_HEIGHT) + 16;
    }

    @Override
    public Inventory createInventory(Component component) {
        return Bukkit.createInventory(null, size(), component);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChestGuiType other)) return false;
        return this.rows == other.rows;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(rows);
    }

}
