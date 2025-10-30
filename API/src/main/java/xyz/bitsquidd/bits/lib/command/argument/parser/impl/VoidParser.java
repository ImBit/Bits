package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;

public final class VoidParser extends AbstractArgumentParser<@NotNull Void> {

    public VoidParser() {
        super(TypeSignature.of(Void.class), "Void");
    }

    @Override
    public @NotNull Void parse(@NotNull List<Object> inputObjects, @NotNull BitsCommandContext ctx) throws CommandParseException {
        return null;
    }

}
