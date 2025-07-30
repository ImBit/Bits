package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.arguments.interfaces.CommandArgument;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.List;

public class StringListArgument extends CommandArgument<String> {
    private final List<String> options;

    private StringListArgument(List<String> options) {
        this.options = options;
    }

    public static StringListArgument of(List<String> options) {
        return new StringListArgument(options);
    }

    public static StringListArgument of(String... options) {
        return new StringListArgument(List.of(options));
    }

    @Override
    public String parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        String input = context.getArg(startIndex);
        for (String option : options) {
            if (option.equalsIgnoreCase(input)) {
                return option;
            }
        }
        throw new ArgumentParseException("Invalid option. Expected one of: " + String.join(", ", options));
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int index) {
        String input = context.getArg(index);
        for (String option : options) {
            if (option.equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull String getTypeName() {
        return options.isEmpty() ? "option" : String.join("|", options);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandContext context, int index) {
        String prefix = context.getArg(index).toLowerCase();
        return options.stream()
              .filter(opt -> opt.toLowerCase().startsWith(prefix))
              .toList();
    }
}