package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BooleanArgumentParser extends PrimitiveArgumentParserNew<@NotNull Boolean> {
    public BooleanArgumentParser() {
        super(Boolean.class, "Boolean");
    }

    @Override
    public @NotNull List<String> getSuggestions() {
        return List.of("true", "false");
    }
}
