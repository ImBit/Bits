package xyz.bitsquidd.bits.lib.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.command.tab.TabCompleter;

public class CommandArgument {
    public final @NotNull String name;
    public final @NotNull String description;
    public final boolean required;
    public final @Nullable TabCompleter tabCompleter;

    public CommandArgument(@NotNull String name, @NotNull String description, boolean required, @Nullable TabCompleter tabCompleter) {
        this.name = name;
        this.description = description;
        this.required = required;
        this.tabCompleter = tabCompleter;
    }
}