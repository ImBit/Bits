package xyz.bitsquidd.bits.lib.command.arguments.interfaces;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

public abstract class AbstractNumberArgument<T extends Number> extends CommandArgument<T> {
    protected final T min;
    protected final T max;

    protected AbstractNumberArgument(T min, T max) {
        this.min = min;
        this.max = max;
    }

    protected abstract T parseValue(String value) throws NumberFormatException;

    @Override
    public abstract @NotNull String getTypeName();

    @Override
    public T parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        if (startIndex >= context.getArgsLength()) {
            throw new ArgumentParseException("No " + getTypeName().toLowerCase() + " argument provided");
        }

        T value;
        try {
            value = parseValue(context.getArgs()[startIndex]);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("Cannot parse '" + context.getArgs()[startIndex] + "' as a " + getTypeName().toLowerCase());
        }

        if (min != null && value.doubleValue() < min.doubleValue()) {
            throw new ArgumentParseException("Value " + value + " is less than minimum allowed " + min);
        }
        if (max != null && value.doubleValue() > max.doubleValue()) {
            throw new ArgumentParseException("Value " + value + " is greater than maximum allowed " + max);
        }

        return value;
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int argIndex) {
        if (argIndex >= context.getArgsLength()) {
            return false;
        }
        try {
            T value = parseValue(context.getArgs()[argIndex]);
            if ((min != null && value.doubleValue() < min.doubleValue()) ||
                    (max != null && value.doubleValue() > max.doubleValue())) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }
}