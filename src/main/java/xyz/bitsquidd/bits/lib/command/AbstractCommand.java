package xyz.bitsquidd.bits.lib.command;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.annotations.Command;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.examples.CommandReturnDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractCommand {
    protected final @NotNull List<CommandPath> paths = new ArrayList<>();

    public final @NotNull String name;
    public final @NotNull String[] aliases;
    public final @NotNull String description;
    public final @NotNull String commandPermission;

    public AbstractCommand() {
        Command annotation = getClass().getAnnotation(Command.class);
        if (annotation == null) {
            throw new IllegalStateException("Command classes must have the @Command annotation");
        }

        this.name = annotation.name();
        this.aliases = annotation.aliases();
        this.description = annotation.description();
        this.commandPermission = annotation.permission();

        initialisePaths();
    }

    protected abstract void showUsage(@NotNull CommandContext commandContext);
    public abstract void initialisePaths();

    public void addPath(CommandPath commandPath) {
        paths.add(commandPath);
    }

    public boolean defaultExecute(CommandContext commandContext) {
        // Executes every time this command is called, even if there are no args.
        return false;
    }

    public boolean vanillaExecute(CommandContext commandContext) {
        try {
            if (hasPermission(commandContext)) executeCommand(commandContext);
            return true;
        } catch (Exception e) {
            Text.of("<b>An unexpected error occurred:</b> " + e.getMessage(), new CommandReturnDecorator(CommandReturnType.ERROR)).send(commandContext.getSender());
            Bukkit.getLogger().severe(e.getMessage());
        }
        return false;
    }

    private void executeCommand(CommandContext commandContext) {
        boolean hasExecutedPath = false;

        try {
            defaultExecute(commandContext);
            // We only execute the first matching path.
            for (CommandPath path : getValidCommandPaths(commandContext)) {
                hasExecutedPath = path.execute(commandContext);
                break;
            }
        } catch (Exception e) {
            Text.of("<b>Error executing command:</b> " + e.getMessage(), new CommandReturnDecorator(CommandReturnType.ERROR)).send(commandContext.getSender());
            Bukkit.getLogger().severe(e.getMessage());
        }

        if (!hasExecutedPath) {
            showUsage(commandContext);
        }
    }

    public List<String> tabComplete(@NotNull CommandContext commandContext) {
        List<String> availableCompletions = new ArrayList<>();

        for (CommandPath path : getValidTabCompletablePaths(commandContext)) {
            availableCompletions.addAll(path.tabComplete(commandContext));
        }

        String currentArgument = commandContext.getLastArg().toLowerCase();
        if (!currentArgument.isEmpty()) {
            availableCompletions.removeIf(completion -> !completion.toLowerCase().startsWith(currentArgument));
        }

        return availableCompletions;
    }

    private Set<CommandPath> getValidTabCompletablePaths(CommandContext commandContext) {
        return paths.stream()
                .filter(path -> path.matchesPartial(commandContext))
                .collect(Collectors.toSet());
    }

    private Set<CommandPath> getValidCommandPaths(CommandContext commandContext) {
        return paths.stream()
                .filter(path -> path.matchesFully(commandContext))
                .collect(Collectors.toSet());
    }

    private boolean hasPermission(CommandContext commandContext) {
        if (commandPermission.isEmpty()) {
            return true;
        } else {
            return commandContext.getSender().hasPermission(commandPermission);
        }
    }
}
