/*
 * This file is part of the BiomeBattle project.
 *
 * Copyright (c) 2023-2026 BitSquidd - All Rights Reserved
 *
 * Unauthorized copying, distribution, or disclosure of this file, via any medium, is strictly prohibited.
 * Proprietary and confidential - for internal use only.
 */

package xyz.bitsquidd.bits.mc.sendable.text;

import java.util.Arrays;

/**
 * Represents a 2D text offset.
 *
 * @param x The right-horizontal offset. (+ve is right)
 * @param y The down-vertical offset. (+ve is down)
 *
 * @since 0.0.13
 */
public record Offset(
  int x,
  int y
) {
    public static final Offset ZERO = new Offset(0, 0);

    public static Offset of(int x, int y) {
        return new Offset(x, y);
    }

    public Offset add(Offset... other) {
        int addedX = Arrays.stream(other).mapToInt(offset -> offset.x).sum();
        int addedY = Arrays.stream(other).mapToInt(offset -> offset.y).sum();
        return new Offset(this.x + addedX, this.y + addedY);
    }

}
