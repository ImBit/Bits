package xyz.bitsquidd.bits.lib.command.argument.impl.primitive;

import org.jetbrains.annotations.NotNull;

public final class BooleanArgumentParser extends PrimitiveArgumentParserNew<@NotNull Boolean> {
    public BooleanArgumentParser() {
        super(Boolean.class, "Boolean");
    }

}
