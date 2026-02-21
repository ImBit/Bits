package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrapper.type.TypeSignature;

import java.util.List;

/**
 * Argument parser for Void variables.
 */
public final class VoidParser extends AbstractArgumentParser<Void> {

    public VoidParser() {
        super(TypeSignature.of(Void.class), "Void");
    }

    @Override
    public Void parse(List<Object> inputObjects, BitsCommandContext<?> ctx) {
        return null;
    }

}
