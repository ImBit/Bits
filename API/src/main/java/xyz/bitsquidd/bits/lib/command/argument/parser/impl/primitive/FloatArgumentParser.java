package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import org.jetbrains.annotations.NotNull;

public final class FloatArgumentParser extends PrimitiveArgumentParserNew<@NotNull Float> {
    public FloatArgumentParser() {
        super(Float.class, "Float");
    }

}
