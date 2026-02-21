/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.util.wrapper;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * A collection of utilities for working with Collections.
 */
public final class CollectionHelper {
    private CollectionHelper() {}

    /**
     * Sets the element at the specified index in the list.
     * If the index is out of bounds, it will automatically expand the list and fill any new positions with the provided default value, which can be null.
     */
    public static <T> List<@Nullable T> setAndPopulate(List<@Nullable T> list, int index, T element, Supplier<@Nullable T> defaultValue) {
        while (list.size() <= index) {
            list.add(defaultValue.get());
        }
        list.set(index, element);
        return list;
    }

    /**
     * Optional getter for lists that returns an empty Optional if the index is out of bounds, instead of throwing an exception.
     */
    public static <T> Optional<T> get(List<T> list, int index) {
        if (index < 0 || index >= list.size()) return Optional.empty();
        return Optional.of(list.get(index));
    }

    /**
     * Sets the element at the specified index in the list.
     * If the index is out of bounds, it will automatically expand the list and fill any new positions with the provided default value.
     */
    public static <T> void set(List<T> list, int index, T element, Supplier<T> defaultValue) {
        if (index < 0) throw new IndexOutOfBoundsException("Index cannot be negative");
        while (list.size() <= index) {
            list.add(defaultValue.get());
        }
        list.set(index, element);
    }

    /**
     * Iterates over the list, providing both the index and the element to the given consumer.
     */
    public static <T> void forEach(List<T> list, BiConsumer<Integer, T> consumer) {
        for (int i = 0; i < list.size(); i++) {
            consumer.accept(i, list.get(i));
        }
    }

    /**
     * Shifts the elements of the list to the right by the specified amount, wrapping around the end of the list.
     */
    public static <T> List<T> shift(List<T> list, int shiftAmount) {
        if (list.isEmpty()) return list;

        int size = list.size();
        shiftAmount = ((shiftAmount % size) + size) % size; // Normalize shift amount

        if (shiftAmount == 0) return list;

        List<T> temp = List.copyOf(list);
        for (int i = 0; i < size; i++) {
            list.set((i + shiftAmount) % size, temp.get(i));
        }

        return list;
    }

}
