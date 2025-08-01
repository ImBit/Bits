package xyz.bitsquidd.bits.lib.command;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;
import xyz.bitsquidd.bits.lib.command.requirements.CommandRequirement;

import java.util.ArrayList;
import java.util.List;

// Sub-command paths.
// TODO: allow for nested command paths - i.e. Whitelist mode/limit/add CommandPath1/2/3
public class CommandPath {
    public final @NotNull String name;
    public final @NotNull String description;

    private final @NotNull List<CommandRequirement> requirements;
    private final @NotNull List<CommandArgumentInfo<?>> params;
    private final @NotNull CommandHandler handler;

    public CommandPath(@NotNull String name, @NotNull String description, @NotNull List<CommandRequirement> requirements, @NotNull List<CommandArgumentInfo<?>> params, @NotNull CommandHandler handler) {
        this.name = name;
        this.description = description;
        this.requirements = requirements;
        this.params = params;
        this.handler = handler;
    }


    public final boolean matchesPartial(@NotNull CommandContext commandContext) {
        int argLength = commandContext.getArgLength();

        if (getArgLength() == 0 && argLength == 0) {
            return true;
        }

        if (argLength > getArgLength()) {
            return false;
        }

        for (int i = 0; i < argLength - 1; i++) { // We subtract 1 here as to help partial tab completion
            CommandArgumentInfo<?> commandArgumentInfo = getCommandParamAtIndex(i);
            if (!commandContext.getArg(i).isEmpty() && !commandArgumentInfo.param.canParseArg(commandContext, i)) {
                return false;
            }
        }

        return true;
    }

    public final boolean matchesFully(@NotNull CommandContext commandContext) {
        int argLength = commandContext.getArgLength();
        int actualArgLength = getArgLength();

        if (actualArgLength == 0 && argLength == 0) {
            return true;
        }

        if (argLength != actualArgLength && params.stream().noneMatch(arg -> arg.param.isGreedy())) {
            return false;
        }

        int argIndex = 0;
        for (CommandArgumentInfo<?> commandArgumentInfo : params) {
            if (!commandArgumentInfo.param.canParseFull(commandContext, argIndex)) {
                return false;
            }
            argIndex += commandArgumentInfo.param.getRequiredArgs();
        }

        return true;
    }

    public int getArgLength() {
        int argLength = 0;
        for (CommandArgumentInfo<?> commandArgumentInfo : params) {
            argLength += commandArgumentInfo.param.getRequiredArgs();
        }

        return argLength;
    }

    private @NotNull CommandArgumentInfo<?> getCommandParamAtIndex(int index) {
        for (CommandArgumentInfo<?> commandArgumentInfo : params) {
            if (index < commandArgumentInfo.param.getRequiredArgs()) {
                return commandArgumentInfo;
            }
            index -= commandArgumentInfo.param.getRequiredArgs();
        }

        //TODO error here
        return null;
    }

    private int getCommandParamIndex(@NotNull CommandArgumentInfo<?> commandArgumentInfo) {
        int argIndex = 0;
        for (CommandArgumentInfo<?> param : params) {
            if (param.equals(commandArgumentInfo)) {
                return argIndex;
            }
            argIndex += param.param.getRequiredArgs();
        }
        //TODO error here
        return -1;
    }

    public final boolean execute(@NotNull CommandContext commandContext) {
        int argIndex = 0;

        try {
            for (CommandArgumentInfo<?> commandArgumentInfo : params) {
                commandContext.set(commandArgumentInfo.name, commandArgumentInfo.param.parse(commandContext, argIndex));
                argIndex += commandArgumentInfo.param.getRequiredArgs();
            }
        } catch (ArgumentParseException e) {
            Bukkit.getLogger().severe("Command Parsing Exception: " + e.getMessage());
            return false;
        }

        handler.execute(commandContext);
        return true;
    }

    public final @NotNull List<String> tabComplete(@NotNull CommandContext commandContext) {
        CommandArgumentInfo<?> commandArgumentInfo = getCommandParamAtIndex(commandContext.getArgLength() - 1);

        ArrayList<String> availableCompletions = new ArrayList<>(commandArgumentInfo.param.getAddedTabCompletions());
        availableCompletions.addAll(commandArgumentInfo.param.tabComplete(commandContext, getCommandParamIndex(commandArgumentInfo)));

        return availableCompletions;
    }

    public boolean hasPermissions(@NotNull CommandContext commandContext) {
        for (CommandRequirement commandRequirement : requirements) {
            if (!commandRequirement.check(commandContext)) {
                return false;
            }
        }
        return true;
    }

    ///  Getters  ///
    public @NotNull List<CommandRequirement> getRequirements() {
        return requirements;
    }

    public @NotNull List<CommandArgumentInfo<?>> getParams() {
        return params;
    }

    public @NotNull CommandHandler getHandler() {
        return handler;
    }
}
