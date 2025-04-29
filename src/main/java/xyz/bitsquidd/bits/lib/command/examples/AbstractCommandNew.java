package xyz.bitsquidd.bits.lib.command.examples;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.CommandContextNew;
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

    public boolean execute(CommandContextNew commandContextNew) {
        try {
            executeCommand(commandContextNew);
            return true;
        } catch (Exception e) {
            commandContextNew.sender.sendMessage("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private void executeCommand(CommandContextNew commandContextNew) {
        try {
            for (CommandPathNew path : getValidPaths(commandContextNew)) {
                path.execute(commandContextNew);
            }
            return;
        } catch (Exception e) {
            commandContextNew.sender.sendMessage("Error executing command: " + e.getMessage());
            e.printStackTrace();
        }

        // No path matched
        showUsage(commandContextNew);
    }

    public List<String> tabComplete(@NotNull CommandContextNew commandContextNew) {
        List<String> availableCompletions = new ArrayList<>();

        LogController.warning(getValidPaths(commandContextNew) + "");
        for (CommandPathNew path : getValidPaths(commandContextNew)) {
            availableCompletions.addAll(path.tabComplete(commandContextNew));
        }

        return availableCompletions;
    }

    private Set<CommandPathNew> getValidPaths(CommandContextNew commandContextNew) {
        return paths.stream()
                .filter(path -> path.matchesPartial(commandContextNew))
                .collect(Collectors.toSet());
    }

    private void showUsage(CommandContextNew commandContextNew) {
        commandContextNew.sender.sendMessage("USAGE HERE!");
    }
}
