package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.arguments.interfaces.AbstractNumberArgument;

public class IntegerArgument extends AbstractNumberArgument<Integer> {
    public static final IntegerArgument INSTANCE = new IntegerArgument(null, null);

    public IntegerArgument(Integer min, Integer max) {
        super(min, max);
    }

    public static IntegerArgument clamp(int min, int max) {
        return new IntegerArgument(min, max);
    }

    @Override
    protected Integer parseValue(String value) throws NumberFormatException {
        return Integer.parseInt(value);
    }

    @Override
    public @NotNull String getTypeName() {
        return "Int";
    }
}