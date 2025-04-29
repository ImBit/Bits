package xyz.bitsquidd.bits.lib.command.examples;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.CommandArgumentInfo;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.CommandPathNew;
import xyz.bitsquidd.bits.lib.command.annotations.CommandNew;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractCommandNew {
    private final List<CommandPathNew> paths = new ArrayList<>();

    public final String name;
    public final String[] aliases;
    public final String description;
    public final String permissions;

    public AbstractCommandNew() {
        CommandNew annotation = getClass().getAnnotation(CommandNew.class);
        if (annotation == null) {
            throw new IllegalStateException("Command classes must have the @Command annotation");
        }

        this.name = annotation.name();
        this.aliases = annotation.aliases();
        this.description = annotation.description();
        this.permissions = annotation.permission();

        initialisePaths();
    }

    public void addPath(CommandPathNew commandPathNew) {
        paths.add(commandPathNew);
    }

    public abstract void initialisePaths();

    public boolean execute(CommandContext commandContext) {
        try {
            executeCommand(commandContext);
            return true;
        } catch (Exception e) {
            commandContext.sender.sendMessage("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private void executeCommand(CommandContext commandContext) {
        try {
            for (CommandPathNew path : getValidPaths(commandContext)) {
                path.execute(commandContext);
            }
            return;
        } catch (Exception e) {
            commandContext.sender.sendMessage("Error executing command: " + e.getMessage());
            e.printStackTrace();
        }

        // No path matched
        showUsage(commandContext);
    }

    public List<String> tabComplete(@NotNull CommandContext commandContext) {
        List<String> availableCompletions = new ArrayList<>();

        LogController.warning(getValidPaths(commandContext) + "");
        for (CommandPathNew path : getValidPaths(commandContext)) {
            availableCompletions.addAll(path.tabComplete(commandContext));
        }

        String currentArgument = commandContext.getLastArg().toLowerCase();
        if (!currentArgument.isEmpty()) {
            availableCompletions.removeIf(completion -> !completion.toLowerCase().startsWith(currentArgument));
        }

        return availableCompletions;
    }

    private Set<CommandPathNew> getValidPaths(CommandContext commandContext) {
        return paths.stream()
                .filter(path -> path.matchesPartial(commandContext))
                .collect(Collectors.toSet());
    }

    // Add to AbstractCommandNew
    private void showUsage(CommandContext commandContext) {
        commandContext.sender.sendMessage("§6=== " + name + " Command Help ===");

        List<CommandPathNew> availablePaths = paths.stream()
                .filter(path -> hasPermissions(commandContext, path.permission))
                .toList();

        if (availablePaths.isEmpty()) {
            commandContext.sender.sendMessage("§cYou don't have permission to use this command.");
            return;
        }

        for (CommandPathNew path : availablePaths) {
            StringBuilder usage = new StringBuilder("§e/" + name + " ");
            StringBuilder params = new StringBuilder();

            for (CommandArgumentInfo arg : path.params) {
                params.append("<").append(arg.name).append(": ").append(arg.param.getTypeName()).append("> ");
            }

            usage.append(params);
            commandContext.sender.sendMessage(usage + "§7- " + path.description);
        }
    }

    public boolean hasPermissions(CommandContext commandContext, String permission) {
        if (permission == null || permission.isEmpty()) {
            return true;
        }

        return commandContext.sender.hasPermission(permission);
    }
}
