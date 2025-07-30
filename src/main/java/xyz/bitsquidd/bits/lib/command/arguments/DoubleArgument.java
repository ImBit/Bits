package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.arguments.interfaces.AbstractNumberArgument;

public class DoubleArgument extends AbstractNumberArgument<Double> {
    public static final DoubleArgument INSTANCE = new DoubleArgument(null, null);

    public DoubleArgument(Double min, Double max) {
        super(min, max);
    }

    public static DoubleArgument clamp(double min, double max) {
        return new DoubleArgument(min, max);
    }

    @Override
    protected Double parseValue(String value) throws NumberFormatException {
        return Double.parseDouble(value);
    }

    @Override
    public @NotNull String getTypeName() {
        return "Double";
    }
}