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
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Range;

import xyz.bitsquidd.bits.mc.sendable.text.Offset;
import xyz.bitsquidd.bits.paper.gui.slot.GuiSlot;

public sealed abstract class GuiType
  permits ChestGuiType, HopperGuiType {

    public abstract MenuType<?> menuType();

    public abstract int size();

    /**
     * The offset required for the bottom left corner of a blank character to reach the top left of the box.
     */
    public final Offset getPosition(GuiSlot slot) {
        int index = slot.index();
        return switch (slot.slotType()) {
            case GUI -> getTopPosition(index);
            case PLAYER -> {
                int row = index / 9;
                int col = index % 9;

                if (row < 1) {
                    yield new Offset(col * 18, height() + 58);
                } else {
                    row = row - 1;
                    yield new Offset(col * 18, row * 18 + height());
                }
            }
        };
    }

    protected abstract Offset getTopPosition(int slot);

    /**
     * The height until the top left slot of the inventory menu.
     */
    public abstract int height();

    public abstract Inventory createInventory(Component title);


    //region Static Constructors
    public static ChestGuiType chest(@Range(from = 1, to = 6) int rows) {
        return new ChestGuiType(rows);
    }

    public static HopperGuiType hopper() {
        return new HopperGuiType();
    }
    //endregion

}
