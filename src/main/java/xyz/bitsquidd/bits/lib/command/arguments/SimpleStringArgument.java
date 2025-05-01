package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.List;

public class SimpleStringArgument implements CommandArgument<String> {
    public static final SimpleStringArgument INSTANCE = new SimpleStringArgument();

    private SimpleStringArgument() {}

    @Override
    public String parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        return context.getArg(startIndex);
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int index) {
        return !context.getArg(index).isEmpty();
    }

    @Override
    public @NotNull String getTypeName() {
        return "String";
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandContext context, int index) {
        return List.of();
    }
}