/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.gui.slot;

import org.jspecify.annotations.NonNull;

import xyz.bitsquidd.bits.paper.gui.Gui;
import xyz.bitsquidd.bits.paper.gui.type.GuiType;

import java.util.*;
import java.util.stream.IntStream;


/**
 * A slot in a {@link Gui GUI}.
 *
 * @since 0.0.13
 */
public record GuiSlot(
  int index,
  SlotType slotType
) {
    public static Set<GuiSlot> all(GuiType guiType) {
        return new HashSet<>(GuiSlot.ofMultiple(
          SlotType.GUI,
          IntStream.range(0, guiType.size()).toArray()
        ));
    }

    public static Set<GuiSlot> playerAll() {
        return new HashSet<>(GuiSlot.ofMultiple(
          SlotType.PLAYER,
          IntStream.range(0, 36).toArray()
        ));
    }


    public static GuiSlot of(int index) {
        return of(SlotType.GUI, index);
    }

    // Note: Minecraft player slots start at 0,1,2,3... in the hotbar, then top left is 9,10,11... .
    public static GuiSlot of(SlotType slotType, int index) {
        return new GuiSlot(index, slotType);
    }

    public static List<GuiSlot> ofMultiple(SlotType slotType, int... indices) {
        return Arrays.stream(indices).mapToObj(slot -> of(slotType, slot)).toList();
    }

    public static List<GuiSlot> ofMultiple(SlotType slotType, Collection<Integer> indices) {
        return indices.stream().map(slot -> of(slotType, slot)).toList();
    }


    public GuiSlot left() {
        return left(1);
    }

    public GuiSlot left(int amount) {
        if (index < amount) throw new IllegalArgumentException("Cannot move left from index " + index + " by " + amount);
        return new GuiSlot(index - amount, slotType);
    }

    public GuiSlot right() {
        return right(1);
    }

    public GuiSlot right(int amount) {
        return new GuiSlot(index + amount, slotType); // TODO check the right index.
    }


    public boolean isValid(GuiType guiType) {
        return switch (this.slotType) {
            case GUI -> index >= 0 && index < guiType.size();
            case PLAYER -> index >= 0 && index < 36; // TODO crafting grid, offhand, armor?
        };
    }


    //region Java Object overrides
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof GuiSlot(int index1, SlotType type1))) return false;
        return this.index == index1 && this.slotType == type1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, slotType);
    }

    @Override
    public @NonNull String toString() {
        return "GuiSlot{" + "index=" + index + ", slotType=" + slotType + '}';
    }
    //endregion

}
