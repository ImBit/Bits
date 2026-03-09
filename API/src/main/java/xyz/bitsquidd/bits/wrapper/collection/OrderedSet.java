/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.wrapper.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Stream;


/**
 * A simple implementation of an ordered set, which maintains the order of insertion while ensuring uniqueness of elements.
 *
 * @param <E> the type of elements maintained by this set
 */
public final class OrderedSet<E> implements Iterable<E> {
    private final ArrayList<E> list = new ArrayList<>();
    private final HashSet<E> set = new HashSet<>();

    @Override
    public String toString() {
        return "OrderedSet{" + list + '}';
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof OrderedSet<?> that && set.equals(that.set));
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    public Stream<E> stream() {
        return list.stream();
    }


    /**
     * Adds the specified element to this set if it is not already present.
     * If the set did not already contain the specified element, it is added to the end of the list and true is returned.
     * If the set already contained the element, false is returned and the set remains unchanged.
     */
    public boolean add(E element) {
        if (set.add(element)) {
            list.add(element);
            return true;
        }
        return false;
    }

    /**
     * Removes the specified element from this set if it is present.
     * If the set contained the specified element, it is removed from both the list and the set, and true is returned.
     */
    public boolean remove(E element) {
        if (set.remove(element)) {
            list.remove(element);
            return true;
        }
        return false;
    }

    /**
     * Removes all of the elements from this set.
     */
    public boolean clear() {
        list.clear();
        set.clear();
        return true;
    }

    /**
     * Returns true if this set contains no elements.
     */
    public boolean isEmpty() {
        return set.isEmpty();
    }

    /**
     * Returns true if this set contains the specified element.
     */
    public boolean contains(E element) {
        return set.contains(element);
    }

    /**
     * Returns the element at the specified position in this set.
     */
    public E get(int index) {
        return list.get(index);
    }


    /**
     * Returns the number of elements in this set.
     */
    public int size() {
        return list.size();
    }

}