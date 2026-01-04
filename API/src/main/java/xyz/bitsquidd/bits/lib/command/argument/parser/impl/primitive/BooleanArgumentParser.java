package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import java.util.List;
import java.util.function.Supplier;

/**
 * Argument parser for Boolean values.
 */
public final class BooleanArgumentParser extends PrimitiveArgumentParser<Boolean> {
    public BooleanArgumentParser() {
        super(Boolean.class, "Boolean");
    }

    @Override
    public Supplier<List<String>> getSuggestions() {
        return () -> List.of("true", "false");
    }

}
