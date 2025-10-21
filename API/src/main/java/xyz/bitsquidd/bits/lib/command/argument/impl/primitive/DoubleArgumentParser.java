package xyz.bitsquidd.bits.lib.command.argument.impl.primitive;

import org.jetbrains.annotations.NotNull;

public final class DoubleArgumentParser extends PrimitiveArgumentParserNew<@NotNull Double> {
    public DoubleArgumentParser() {
        super(Double.class, "double");
    }

}
