/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command.argument.parser.impl.primitive;

/**
 * Argument parser for Integer values.
 */
public final class IntegerArgumentParser extends PrimitiveArgumentParser<Integer> {
    public IntegerArgumentParser() {
        super(Integer.class, "Integer");
    }

}
