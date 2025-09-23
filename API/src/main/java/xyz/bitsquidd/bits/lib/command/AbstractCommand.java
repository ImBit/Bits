package xyz.bitsquidd.bits.lib.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.annotations.Command;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.CommandReturnDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract base class for commands.
 * <p>
 * Used for commands, including their paths, aliases, descriptions, and permissions.
 * It also handles command execution and tab completion.
 */
public abstract class AbstractCommand {
    protected final @NotNull List<CommandPath> paths = new ArrayList<>();

    public final @NotNull String name;
    public final @NotNull String[] aliases;
    public final @NotNull String description;
    public final @NotNull String commandPermission;

    public AbstractCommand() {
        Command annotation = getClass().getAnnotation(Command.class);
        if (annotation == null) throw new IllegalStateException("Command classes must have the @Command annotation");

        this.name = annotation.name();
        this.aliases = annotation.aliases();
        this.description = annotation.description();
        this.commandPermission = annotation.permission();

        initialisePaths();
    }

    protected abstract void showUsage(@NotNull CommandContext commandContext);

    public abstract void initialisePaths();

    public final void addPath(@NotNull CommandPath commandPath) {
        paths.add(commandPath);
    }

    public boolean defaultExecute(@NotNull CommandContext commandContext) {
        // Executes every time this command is called, even if there are no args.
        return false;
    }

    public final boolean vanillaExecute(@NotNull CommandContext commandContext) {
        try {
            if (hasPermission(commandContext)) executeCommand(commandContext);
            return true;
        } catch (Exception e) {
            Text.of(Component.text("<b>An unexpected error occurred:</b> " + e.getMessage()))
                  .decorate(CommandReturnDecorator.of(CommandReturnType.ERROR))
                  .send(commandContext.getSender());
            Bukkit.getLogger().severe(e.getMessage());
        }
        return false;
    }

    private void executeCommand(@NotNull CommandContext commandContext) {
        boolean hasExecutedPath = false;

        try {
            defaultExecute(commandContext);
            // We only execute the first matching path.
            for (CommandPath path : getValidCommandPaths(commandContext)) {
                hasExecutedPath = path.execute(commandContext);
                break;
            }
        } catch (Exception e) {
            Text.of(Component.text("<b>Error executing command:</b> " + e.getMessage()))
                  .decorate(CommandReturnDecorator.of(CommandReturnType.ERROR))
                  .send(commandContext.getSender());
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

    private Set<CommandPath> getValidTabCompletablePaths(@NotNull CommandContext commandContext) {
        return paths.stream()
              .filter(path -> path.matchesPartial(commandContext))
              .collect(Collectors.toSet());
    }

    private Set<CommandPath> getValidCommandPaths(@NotNull CommandContext commandContext) {
        return paths.stream()
              .filter(path -> path.matchesFully(commandContext))
              .collect(Collectors.toSet());
    }

    private boolean hasPermission(@NotNull CommandContext commandContext) {
        if (commandPermission.isEmpty()) {
            return true;
        } else {
            return commandContext.getSender().hasPermission(commandPermission);
        }
    }
}
