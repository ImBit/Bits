package xyz.bitsquidd.bits.lib.command.argument.impl.primitive;

import org.jetbrains.annotations.NotNull;

public final class IntegerArgumentParser extends PrimitiveArgumentParserNew<@NotNull Integer> {
    public IntegerArgumentParser() {
        super(Integer.class, "Integer");
    }

}
