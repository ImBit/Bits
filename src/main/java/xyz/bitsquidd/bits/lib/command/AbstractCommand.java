package xyz.bitsquidd.bits.lib.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.annotations.Command;
import xyz.bitsquidd.bits.lib.component.color.ColorStore;
import xyz.bitsquidd.bits.lib.logging.LogController;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.examples.CommandReturnDecorator;

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
            Text.of("<b>An unexpected error occurred:</b> " + e.getMessage(), new CommandReturnDecorator(CommandReturnType.ERROR)).send(commandContext.sender);
            LogController.exception(e);
        }
        return false;
    }

    private void executeCommand(CommandContext commandContext) {
        boolean hasExecutedPath;

        try {
            hasExecutedPath = defaultExecute(commandContext);

            if (!commandContext.isEmpty()) {
                for (CommandPath path : getValidPaths(commandContext)) {
                    hasExecutedPath = hasExecutedPath || path.execute(commandContext);
                }
            }
        } catch (Exception e) {
            Text.of("<b>Error executing command:</b> " + e.getMessage(), new CommandReturnDecorator(CommandReturnType.ERROR)).send(commandContext.sender);
            LogController.exception(e);
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
        Component usageComponent = Component.empty().appendNewline().appendNewline().appendNewline();

        usageComponent = usageComponent
                .append(Component.text("          ").decorate(TextDecoration.STRIKETHROUGH))
                .append(Component.text(" /"+name+" ").decorate(TextDecoration.BOLD))
                .append(Component.text("          ").decorate(TextDecoration.STRIKETHROUGH))
                .appendNewline();

        List<CommandPath> availablePaths = paths.stream()
                .filter(path -> path.hasPermissions(commandContext))
                .toList();

        if (availablePaths.isEmpty()) {
            Text.of(
                    "You don't have permission to use this command",
                    new CommandReturnDecorator(CommandReturnType.ERROR)
            ).send(commandContext.sender);
            return;
        }

        for (CommandPath path : availablePaths) {
            usageComponent = usageComponent
                    .append(Component.text("/" + name + " ", ColorStore.INFO.getTextColor()));


            for (CommandArgumentInfo arg : path.getParams()) {
                    String paramText = "<" + arg.name + " : " + arg.param.getTypeName() + "> ";
                    usageComponent = usageComponent.append(
                            Component.text(paramText, ColorStore.INFO.getTextColor())
                    );
            }

            usageComponent = usageComponent
                    .appendNewline()
                    .append(Component.text("  ⏵ " + path.description, NamedTextColor.GRAY)
                    .appendNewline()
            );
        }

        usageComponent = usageComponent.append(
                Component.text("                              ").decorate(TextDecoration.STRIKETHROUGH)
        );

        Text.of(usageComponent).send(commandContext.sender);
    }

    private boolean hasPermission(CommandContext commandContext) {
        if (commandPermission.isEmpty()) {
            return true;
        }
        return commandContext.sender.hasPermission(commandPermission);
    }
}
