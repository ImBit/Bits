package xyz.bitsquidd.bits.paper.lib.command.argument.parser.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrapper.type.TypeSignature;
import xyz.bitsquidd.bits.paper.lib.location.BlockPos;

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
