package xyz.bitsquidd.bits.lib.command;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.command.arguments.ICommandArgument;

// Used to store params and their names in commands.
public class CommandArgumentInfo<T> {
    public final @NotNull String name;
    public final @NotNull ICommandArgument<T> param;
    @ApiStatus.Experimental
    public final @Nullable T defaultValue;

    public CommandArgumentInfo(@NotNull String name, @NotNull ICommandArgument<T> param) {
        this(name, param, null);
    }

    @ApiStatus.Experimental
    public CommandArgumentInfo(@NotNull String name, @NotNull ICommandArgument<T> param, @Nullable T defaultValue) {
        this.name = name;
        this.param = param;
        this.defaultValue = defaultValue;
    }
}
