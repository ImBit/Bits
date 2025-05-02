package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

public class DoubleArgument extends CommandArgument<Double> {
    public static final DoubleArgument INSTANCE = new DoubleArgument();

    @Override
    public @NotNull String getTypeName() {
        return "Double";
    }

    @Override
    public Double parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        if (startIndex >= context.getArgsLength()) {
            throw new ArgumentParseException("No double argument provided");
        }

        try {
            return Double.parseDouble(context.getArgs()[startIndex]);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("Cannot parse '" + context.getArgs()[startIndex] + "' as an double");
        }
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int argIndex) {
        if (argIndex >= context.getArgsLength()) {
            return false;
        }

        try {
            Double.parseDouble(context.getArgs()[argIndex]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}