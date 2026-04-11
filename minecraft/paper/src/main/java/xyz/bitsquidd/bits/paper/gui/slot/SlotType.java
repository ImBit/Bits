/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.gui.slot;

/**
 * The type of a {@link GuiSlot}. This will either be a slot in the container itself, or a slot in the player's inventory.
 *
 * @since 0.0.13
 */
public enum SlotType {
    GUI,    // The item is part of the window.
    PLAYER  // The item is part of the player's inventory. // TODO: consider armor, offhand, crafting grid etc.

}
