package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.type.GreedyString;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;

public final class GreedyStringArgumentParser extends AbstractArgumentParser<@NotNull GreedyString> {
    public GreedyStringArgumentParser() {
        super(TypeSignature.of(GreedyString.class), "String...");
    }

    @Override
    public @NotNull GreedyString parse(@NotNull List<Object> inputObjects, @NotNull BitsCommandContext ctx) throws CommandParseException {
        return GreedyString.of(singletonInputValidation(inputObjects, String.class));
    }

    @Override
    public @NotNull List<InputTypeContainer> getInputTypes() {
        return List.of(new InputTypeContainer(TypeSignature.of(String.class), getArgumentName()));
    }

}
