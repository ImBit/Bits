package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

public class IntegerArgument implements CommandArgument<Integer> {
    public static final IntegerArgument INSTANCE = new IntegerArgument();

    @Override
    public @NotNull String getTypeName() {
        return "Int";
    }

    @Override
    public Integer parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        if (startIndex >= context.args.length) {
            throw new ArgumentParseException("No integer argument provided");
        }

        try {
            return Integer.parseInt(context.args[startIndex]);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("Cannot parse '" + context.args[startIndex] + "' as an integer");
        }
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int argIndex) {
        if (argIndex >= context.args.length) {
            return false;
        }

        try {
            Integer.parseInt(context.args[argIndex]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}