package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrapper.GreedyString;
import xyz.bitsquidd.bits.lib.wrapper.type.TypeSignature;

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
