package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

import java.util.List;

public final class VoidParser extends AbstractArgumentParser<Void> {

    public VoidParser() {
        super(TypeSignature.of(Void.class), "Void");
    }

    @Override
    public Void parse(List<Object> inputObjects, BitsCommandContext<?> ctx) throws CommandParseException {
        return null;
    }

}
