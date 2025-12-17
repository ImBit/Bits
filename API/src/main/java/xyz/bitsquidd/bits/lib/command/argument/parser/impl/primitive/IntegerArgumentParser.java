package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import org.jetbrains.annotations.NotNull;

public final class IntegerArgumentParser extends PrimitiveArgumentParser<Integer> {
    public IntegerArgumentParser() {
        super(Integer.class, "Integer");
    }

}
