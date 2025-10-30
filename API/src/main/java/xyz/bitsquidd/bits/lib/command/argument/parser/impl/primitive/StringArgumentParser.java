package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import org.jetbrains.annotations.NotNull;

public final class StringArgumentParser extends PrimitiveArgumentParser<@NotNull String> {
    public StringArgumentParser() {
        super(String.class, "String");
    }

}
