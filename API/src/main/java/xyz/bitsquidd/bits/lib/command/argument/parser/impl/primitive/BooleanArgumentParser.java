/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import java.util.List;
import java.util.function.Supplier;

/**
 * Argument parser for Boolean values.
 */
public final class BooleanArgumentParser extends PrimitiveArgumentParser<Boolean> {
    public BooleanArgumentParser() {
        super(Boolean.class, "Boolean");
    }

    @Override
    public Supplier<List<String>> getSuggestions() {
        return () -> List.of("true", "false");
    }

}
