package xyz.bitsquidd.bits.lib.command.params;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BooleanArgument implements CommandArgument<Boolean> {
    public static final BooleanArgument INSTANCE = new BooleanArgument();
    
    private static final Set<String> TRUE_VALUES = new HashSet<>(Arrays.asList(
            "true", "enable"
    ));
    
    private static final Set<String> FALSE_VALUES = new HashSet<>(Arrays.asList(
            "false", "disable"
    ));

    @Override
    public @NotNull String getTypeName() {
        return "Boolean";
    }

    @Override
    public Boolean parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        if (startIndex >= context.args.length) {
            throw new ArgumentParseException("No boolean argument provided");
        }

        String input = context.args[startIndex].toLowerCase();
        
        if (TRUE_VALUES.contains(input)) {
            return true;
        }
        
        if (FALSE_VALUES.contains(input)) {
            return false;
        }

        throw new ArgumentParseException("Cannot parse '" + context.args[startIndex] + "' as a boolean");
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int argIndex) {
        if (argIndex >= context.args.length) {
            return false;
        }

        String input = context.args[argIndex].toLowerCase();
        return TRUE_VALUES.contains(input) || FALSE_VALUES.contains(input);
    }
}