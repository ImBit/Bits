package xyz.bitsquidd.bits.lib.command.arguments.interfaces;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.arguments.ICommandArgument;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandArgument<T> implements ICommandArgument<T> {
    protected ArrayList<String> addedTabCompletions = new ArrayList<>();

    @Override
    public void addTabCompletions(List<String> completions) {
        addedTabCompletions.addAll(completions);
    }

    @Override
    public @NotNull List<String> getAddedTabCompletions() {
        return addedTabCompletions;
    }
}
