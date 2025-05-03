package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.*;

public class BooleanArgument extends CommandArgument<Boolean> {
    public static final BooleanArgument INSTANCE = new BooleanArgument();
    
    private static final Set<String> TRUE_VALUES = new HashSet<>(Arrays.asList(
            "true", "enable", "1"
    ));
    
    private static final Set<String> FALSE_VALUES = new HashSet<>(Arrays.asList(
            "false", "disable", "0"
    ));

    @Override
    public @NotNull String getTypeName() {
        return "Boolean";
    }

    @Override
    public Boolean parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        if (startIndex >= context.getArgsLength()) {
            throw new ArgumentParseException("No boolean argument provided");
        }

        String input = context.getArgs()[startIndex].toLowerCase();
        
        if (TRUE_VALUES.contains(input)) {
            return true;
        }
        
        if (FALSE_VALUES.contains(input)) {
            return false;
        }

        throw new ArgumentParseException("Cannot parse '" + context.getArgs()[startIndex] + "' as a boolean");
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int argIndex) {
        if (argIndex >= context.getArgsLength()) {
            return false;
        }

        String input = context.getArgs()[argIndex].toLowerCase();
        return TRUE_VALUES.contains(input) || FALSE_VALUES.contains(input);
    }

    @Override
    public @NotNull List<String> tabComplete(CommandContext context, int startIndex) {
        ArrayList<String> completions = new ArrayList<>(TRUE_VALUES);
        completions.addAll(FALSE_VALUES);

        return completions;
    }
}