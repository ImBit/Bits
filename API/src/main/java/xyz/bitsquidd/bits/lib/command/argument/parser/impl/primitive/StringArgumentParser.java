/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

/**
 * Argument parser for String values.
 */
public final class StringArgumentParser extends PrimitiveArgumentParser<String> {
    public StringArgumentParser() {
        super(String.class, "String");
    }

}
