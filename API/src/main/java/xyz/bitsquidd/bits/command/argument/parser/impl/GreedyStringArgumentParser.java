/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command.argument.parser.impl;

import xyz.bitsquidd.bits.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.wrapper.GreedyString;
import xyz.bitsquidd.bits.wrapper.type.TypeSignature;

import java.util.List;

/**
 * Argument parser for GreedyString values.
 * A {@link GreedyString} is special as it captures all remaining input as a single string.
 */
public final class GreedyStringArgumentParser extends AbstractArgumentParser<GreedyString> {
    public GreedyStringArgumentParser() {
        super(TypeSignature.of(GreedyString.class), "String...");
    }

    @Override
    public GreedyString parse(List<Object> inputObjects, BitsCommandContext<?> ctx) {
        return GreedyString.of((String)inputObjects.getFirst()); // Note: can't use singletonInputValidation as GreedyString is special...
    }

    @Override
    public List<InputTypeContainer> getInputTypes() {
        return List.of(new InputTypeContainer(TypeSignature.of(GreedyString.class), getArgumentName()));
    }

}
