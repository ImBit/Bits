package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.location.BlockPos;

import java.util.List;

public final class BlockPosArgumentParser extends AbstractArgumentParser<@NotNull BlockPos> {

    public BlockPosArgumentParser() {
        super(TypeSignature.of(BlockPos.class), "BlockPos");
    }

    @Override
    public @NotNull BlockPos parse(@NotNull List<Object> inputObjects, @NotNull BitsCommandContext ctx) throws CommandParseException {
        List<Object> inputs = inputValidation(inputObjects);
        double x = (double)inputs.get(0);
        double y = (double)inputs.get(1);
        double z = (double)inputs.get(2);
        return BlockPos.of(x, y, z);
    }

    @Override
    public @NotNull List<InputTypeContainer> getInputTypes() {
        return List.of(
              new InputTypeContainer(TypeSignature.of(Double.class), "x"),
              new InputTypeContainer(TypeSignature.of(Double.class), "y"),
              new InputTypeContainer(TypeSignature.of(Double.class), "z")
        );
    }

}
