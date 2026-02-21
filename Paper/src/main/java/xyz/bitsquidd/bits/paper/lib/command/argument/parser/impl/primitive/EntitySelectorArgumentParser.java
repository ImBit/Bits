/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.lib.command.argument.parser.impl.primitive;

import net.minecraft.commands.arguments.selector.EntitySelector;

import xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive.PrimitiveArgumentParser;

/**
 * Argument parser for EntitySelector values.
 */
public final class EntitySelectorArgumentParser extends PrimitiveArgumentParser<EntitySelector> {
    public EntitySelectorArgumentParser() {
        super(EntitySelector.class, "EntitySelector");
    }

}
