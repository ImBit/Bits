package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import org.jetbrains.annotations.NotNull;

public final class LongArgumentParser extends PrimitiveArgumentParser<@NotNull Long> {
    public LongArgumentParser() {
        super(Long.class, "Long");
    }

}
