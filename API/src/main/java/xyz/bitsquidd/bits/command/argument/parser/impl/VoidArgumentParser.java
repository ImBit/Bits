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
 * Argument parser for Void variables.
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
