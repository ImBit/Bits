package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.type.GreedyString;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

import java.util.List;

public final class GreedyStringArgumentParser extends AbstractArgumentParser<@NotNull GreedyString> {
    public GreedyStringArgumentParser() {
        super(TypeSignature.of(GreedyString.class), "String...");
    }

    @Override
    public @NotNull GreedyString parse(@NotNull List<Object> inputObjects, @NotNull BitsCommandContext ctx) throws CommandParseException {
        return GreedyString.of((String)inputObjects.getFirst()); // Note: can't use singletonInputValidation as GreedyString is special...
    }

    @Override
    public @NotNull List<InputTypeContainer> getInputTypes() {
        return List.of(new InputTypeContainer(TypeSignature.of(GreedyString.class), getArgumentName()));
    }

}
