package xyz.bitsquidd.bits.lib.command.argument.impl.primitive;

import org.jetbrains.annotations.NotNull;

public final class StringArgumentParser extends PrimitiveArgumentParserNew<@NotNull String> {
    public StringArgumentParser() {
        super(String.class, "string");
    }

}
