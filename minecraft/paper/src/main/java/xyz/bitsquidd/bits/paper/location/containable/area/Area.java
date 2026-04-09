/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.location.containable.area;

import com.google.common.collect.ImmutableList;

import xyz.bitsquidd.bits.lifecycle.builder.Buildable;
import xyz.bitsquidd.bits.paper.location.containable.Containable;
import xyz.bitsquidd.bits.paper.location.wrapper.Locatable;

import java.util.ArrayList;
import java.util.List;

/**
 * An immutable boolean composition of {@link Containable}s (Regions or other Areas),
 * evaluated left-to-right as a sequence of set operations.
 *
 * <pre>{@code
 * Area arena = Area.from(floor)
 *     .union(upperTier)
 *     .subtract(spawnPlatform)
 *     .intersect(mapBounds)
 *     .build();
 * }</pre>
 * <p>
 *
 * @since 0.0.13
 */
public final class Area implements Containable {
    public enum Operation {
        UNION,
        SUBTRACT,
        INTERSECT
    }

    public record AreaEntry(
      Containable containable,
      Operation operation
    ) {}

    private final ImmutableList<AreaEntry> entries;

    private Area(List<AreaEntry> entries) {
        if (entries.isEmpty()) throw new IllegalArgumentException("Area must have at least one entry");
        this.entries = ImmutableList.copyOf(entries);
    }

    /**
     * Begin building an Area, seeding it with an initial containable via UNION.
     */
    public static Builder from(Containable initial) {
        return new Builder(initial);
    }

    /**
     * Evaluates the operation chain left to right.
     * Starts from {@code false} (empty set) and applies each operation in order.
     */
    @Override
    public boolean contains(Locatable locatable) {
        if (locatable == null) return false;

        boolean result = false;

        for (AreaEntry entry : entries) {
            boolean inside = entry.containable().contains(locatable);
            result = switch (entry.operation()) {
                case UNION -> result || inside;
                case SUBTRACT -> result && !inside;
                case INTERSECT -> result && inside;
            };
        }

        return result;
    }


    public static final class Builder implements Buildable<Area> {
        private final List<AreaEntry> entries = new ArrayList<>();

        private Builder(Containable initial) {
            union(initial);
        }

        public Builder union(Containable containable) {
            entries.add(new AreaEntry(containable, Operation.UNION));
            return this;
        }

        public Builder subtract(Containable containable) {
            entries.add(new AreaEntry(containable, Operation.SUBTRACT));
            return this;
        }

        public Builder intersect(Containable containable) {
            entries.add(new AreaEntry(containable, Operation.INTERSECT));
            return this;
        }

        @Override
        public Area build() {
            return new Area(new ArrayList<>(entries));
        }

    }

}