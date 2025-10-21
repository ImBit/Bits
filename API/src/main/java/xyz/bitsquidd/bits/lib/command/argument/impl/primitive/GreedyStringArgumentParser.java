package xyz.bitsquidd.bits.lib.command.argument.impl.primitive;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.type.GreedyString;

public final class GreedyStringArgumentParser extends PrimitiveArgumentParserNew<@NotNull GreedyString> {
    public GreedyStringArgumentParser() {
        super(GreedyString.class, "String...");
    }

}
