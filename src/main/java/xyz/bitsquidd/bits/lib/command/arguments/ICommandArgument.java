package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.Collections;
import java.util.List;

public interface ICommandArgument<T> {
    String getTypeName();

    T parse(CommandContext context, int startIndex) throws ArgumentParseException;

    boolean canParseArg(CommandContext context, int argIndex);

    default int getRequiredArgs() {
        return 1;
    }

    default @NotNull List<String> tabComplete(CommandContext context, int startIndex) {
        return Collections.emptyList();
    }

    void addTabCompletions(List<String> completions);

    @NotNull List<String> getAddedTabCompletions();

    default boolean canParseFull(CommandContext context, int startIndex) {
        try {
            parse(context, startIndex);
            return true;
        } catch (ArgumentParseException e) {
            return false;
        }
    }

    public default boolean isGreedy() {
        return false; // Note: if an argument is greedy, RequiredArgs would not be necessary (as long as it appears at the end of the command).
    }

}