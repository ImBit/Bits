package xyz.bitsquidd.bits.lib.command.argument.impl.primitive;

import org.jetbrains.annotations.NotNull;

public final class LongArgumentParser extends PrimitiveArgumentParserNew<@NotNull Long> {
    public LongArgumentParser() {
        super(Long.class, "long");
    }

}
