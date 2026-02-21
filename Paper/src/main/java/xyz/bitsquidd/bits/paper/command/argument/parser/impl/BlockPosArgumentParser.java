/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.command.argument.parser.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import xyz.bitsquidd.bits.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.wrapper.type.TypeSignature;
import xyz.bitsquidd.bits.paper.location.BlockPos;

import java.util.List;

public final class BlockPosArgumentParser extends AbstractArgumentParser<BlockPos> {

    public BlockPosArgumentParser() {
        super(TypeSignature.of(BlockPos.class), "BlockPos");
    }

    @Override
    public BlockPos parse(List<Object> inputObjects, BitsCommandContext<?> ctx) throws CommandSyntaxException {
        List<Object> inputs = inputValidation(inputObjects);
        double x = (double)inputs.get(0);
        double y = (double)inputs.get(1);
        double z = (double)inputs.get(2);
        return BlockPos.of(x, y, z);
    }

    @Override
    public List<InputTypeContainer> getInputTypes() {
        return List.of(
          new InputTypeContainer(TypeSignature.of(Double.class), "x"),
          new InputTypeContainer(TypeSignature.of(Double.class), "y"),
          new InputTypeContainer(TypeSignature.of(Double.class), "z")
        );
    }

}
