/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.location.containable;

import org.bukkit.World;

import xyz.bitsquidd.bits.paper.location.wrapper.BlockPos;
import xyz.bitsquidd.bits.paper.location.wrapper.Locatable;

public interface Containable {
    boolean contains(Locatable locatable);

    World world();

    BlockPos center();

    BlockPos min();

    BlockPos max();

}