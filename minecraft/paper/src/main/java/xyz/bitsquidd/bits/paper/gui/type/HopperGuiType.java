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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import xyz.bitsquidd.bits.mc.sendable.text.Offset;

public final class HopperGuiType extends GuiType {

    HopperGuiType() {}

    @Override
    public MenuType<?> menuType() {
        return MenuType.HOPPER;
    }

    @Override
    public int size() {
        return 5;
    }

    @Override
    public Offset getTopPosition(int slot) {
        return Offset.of(36 + (slot * 18), 4);
    }

    @Override
    public int height() {
        return 0;
    }

    @Override
    public Inventory createInventory(Component component) {
        return Bukkit.createInventory(null, InventoryType.HOPPER, component);
    }


    //region Java Object Overrides
    @Override
    public boolean equals(Object obj) {
        return obj instanceof HopperGuiType;
    }

    @Override
    public int hashCode() {
        return HopperGuiType.class.hashCode();
    }
    //endregion

}
