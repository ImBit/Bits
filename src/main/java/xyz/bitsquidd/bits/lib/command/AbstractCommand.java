package xyz.bitsquidd.bits.lib.command;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.annotations.CommandNew;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractCommand {
    private final List<CommandPath> paths = new ArrayList<>();

    public final @NotNull String name;
    public final @NotNull String[] aliases;
    public final @NotNull String description;
    public final @NotNull String commandPermission;

    public AbstractCommand() {
        CommandNew annotation = getClass().getAnnotation(CommandNew.class);
        if (annotation == null) {
            throw new IllegalStateException("Command classes must have the @Command annotation");
        }

        this.name = annotation.name();
        this.aliases = annotation.aliases();
        this.description = annotation.description();
        this.commandPermission = annotation.permission();

        initialisePaths();
    }

    public void addPath(CommandPath commandPath) {
        paths.add(commandPath);
    }

    public abstract void initialisePaths();

    public boolean defaultExecute(CommandContext commandContext) {
        // Executes every time this command is called, even if there are no args.
        return false;
    }

    public boolean execute(CommandContext commandContext) {
        try {
            if (hasPermission(commandContext)) {
                executeCommand(commandContext);
            }
            return true;
        } catch (Exception e) {
            commandContext.sender.sendMessage("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private void executeCommand(CommandContext commandContext) {
        boolean hasExecutedPath = false;

        try {
            hasExecutedPath = hasExecutedPath || defaultExecute(commandContext);

            if (!commandContext.isEmpty()) {
                for (CommandPath path : getValidPaths(commandContext)) {
                    hasExecutedPath = hasExecutedPath || path.execute(commandContext);
                }
            }
        } catch (Exception e) {
            commandContext.sender.sendMessage("Error executing command: " + e.getMessage());
            e.printStackTrace();
            hasExecutedPath = false;
        }

        if (!hasExecutedPath) {
            showUsage(commandContext);
        }
    }

    public List<String> tabComplete(@NotNull CommandContext commandContext) {
        List<String> availableCompletions = new ArrayList<>();

        for (CommandPath path : getValidPaths(commandContext)) {
            availableCompletions.addAll(path.tabComplete(commandContext));
        }

        String currentArgument = commandContext.getLastArg().toLowerCase();
        if (!currentArgument.isEmpty()) {
            availableCompletions.removeIf(completion -> !completion.toLowerCase().startsWith(currentArgument));
        }

        return availableCompletions;
    }

    private Set<CommandPath> getValidPaths(CommandContext commandContext) {
        return paths.stream()
                .filter(path -> path.matchesPartial(commandContext))
                .collect(Collectors.toSet());
    }

    private void showUsage(CommandContext commandContext) {
        commandContext.sender.sendMessage("§6=== " + name + " Command Help ===");

        List<CommandPath> availablePaths = paths.stream()
                .filter(path -> path.hasPermissions(commandContext))
                .toList();

        if (availablePaths.isEmpty()) {
            commandContext.sender.sendMessage("§cYou don't have permission to use this command.");
            return;
        }

        for (CommandPath path : availablePaths) {
            StringBuilder usage = new StringBuilder("§e/" + name + " ");
            StringBuilder params = new StringBuilder();

            for (CommandArgumentInfo arg : path.getParams()) {
                params.append("<").append(arg.name).append(" : ").append(arg.param.getTypeName()).append("> ");
            }

            usage.append(params);
            commandContext.sender.sendMessage(usage + "§7- " + path.description);
        }
    }

    private boolean hasPermission(CommandContext commandContext) {
        if (commandPermission.isEmpty()) {
            return true;
        }
        return commandContext.sender.hasPermission(commandPermission);
    }
}
