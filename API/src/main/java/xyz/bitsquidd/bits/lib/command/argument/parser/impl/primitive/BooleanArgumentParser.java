package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public final class BooleanArgumentParser extends PrimitiveArgumentParserNew<@NotNull Boolean> {
    public BooleanArgumentParser() {
        super(Boolean.class, "Boolean");
    }

    @Override
    public @Nullable Supplier<List<String>> getSuggestions() {
        return () -> List.of("true", "false");
    }
}
