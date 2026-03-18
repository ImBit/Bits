/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command.argument.parser.impl;

import xyz.bitsquidd.bits.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.wrapper.type.TypeSignature;

import java.util.List;

/**
 * A fallback argument parser that explicitly consumes and returns {@code null} for {@link Void} types.
 * <p>
 * Usually dispatched when no proper parser exists for an unregistered command parameter type.
 *
 * @since 0.0.10
 */
public final class VoidArgumentParser extends AbstractArgumentParser<Void> {

    public VoidArgumentParser() {
        super(TypeSignature.of(Void.class), "Void");
    }

    @Override
    public Void parse(List<Object> inputObjects, BitsCommandContext<?> ctx) {
        return null;
    }

}
