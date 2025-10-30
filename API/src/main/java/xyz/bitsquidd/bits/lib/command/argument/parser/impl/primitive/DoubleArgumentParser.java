package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import org.jetbrains.annotations.NotNull;

public final class DoubleArgumentParser extends PrimitiveArgumentParser<@NotNull Double> {
    public DoubleArgumentParser() {
        super(Double.class, "Double");
    }

}
