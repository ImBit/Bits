package xyz.bitsquidd.bits.lib.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.command.tab.TabCompleter;
import xyz.bitsquidd.bits.lib.command.tab.example.NodeTabCompleter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CommandNode {
    public final @NotNull String name;
    public final @NotNull String description;
    public final @NotNull String permission;
    private final @NotNull List<TabCompleter<?>> tabCompleters = new ArrayList<>();
    private final @Nullable BiFunction<CommandSender, String[], Boolean> executor;
    private final @NotNull Set<CommandNode> children = new HashSet<>();
    private final boolean showInCompletion;

    public CommandNode(
            @NotNull String name,
            @NotNull String description,
            @NotNull String permission,
            @Nullable List<TabCompleter<?>> tabCompleters,
            @Nullable BiFunction<CommandSender, String[], Boolean> executor,
            boolean showInCompletion) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.executor = executor;
        this.showInCompletion = showInCompletion;

        this.tabCompleters.add(NodeTabCompleter.of(this));

        if (tabCompleters != null && !tabCompleters.isEmpty()) {
            this.tabCompleters.addAll(tabCompleters);
        }
    }

    public boolean hasPermission() {
        return !permission.isEmpty();
    }

    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (executor == null) {
            return false;
        }

        if (args.length > 0 && !tabCompleters.isEmpty()) {
            String input = args[0];

            for (TabCompleter<?> completer : tabCompleters) {
                Object value = completer.getValue(input);
                if (value != null) {
                    return executor.apply(sender, args);
                }
            }

            List<String> suggestions = new ArrayList<>();
            for (TabCompleter<?> completer : tabCompleters) {
                suggestions.addAll(completer.getPossibleValues(sender));
            }

            if (!suggestions.isEmpty()) {
                sender.sendMessage("§cInvalid input: §f" + input);

                List<String> closestMatches = suggestions.stream()
                        .filter(s -> calculateSimilarity(s.toLowerCase(), input.toLowerCase()) > 0.5)
                        .limit(3)
                        .collect(Collectors.toList());

                if (!closestMatches.isEmpty()) {
                    sender.sendMessage("§6Did you mean: §f" + String.join(", ", closestMatches));
                }
            }

            return true;
        }

        return executor.apply(sender, args);
    }

    private double calculateSimilarity(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        int[][] distance = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) distance[i][0] = i;
        for (int j = 0; j <= len2; j++) distance[0][j] = j;

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                distance[i][j] = Math.min(
                        Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1),
                        distance[i - 1][j - 1] + cost
                );
            }
        }

        int maxLen = Math.max(len1, len2);
        return maxLen == 0 ? 1.0 : 1.0 - ((double) distance[len1][len2] / maxLen);
    }

    public CommandNode addChild(@NotNull CommandNode child) {
        children.add(child);
        return this;
    }

    public boolean isHiddenFromCompletion() {
        return !showInCompletion;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public boolean hasTabCompleters() {
        return !tabCompleters.isEmpty();
    }

    public @NotNull List<TabCompleter<?>> getTabCompleters() {
        return tabCompleters;
    }

    public @NotNull List<String> getChildrenNames() {
        return children.stream()
                .filter(node -> !node.isHiddenFromCompletion())
                .map(node -> node.name)
                .collect(Collectors.toList());
    }

    public @Nullable CommandNode getChild(@NotNull String name) {
        String lowerName = name.toLowerCase();
        return children.stream()
                .filter(child -> child.name.toLowerCase().equals(lowerName))
                .findFirst()
                .orElse(null);
    }

    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String currentArg) {
        List<String> suggestions = new ArrayList<>();

        for (TabCompleter<?> completer : tabCompleters) {
            suggestions.addAll(completer.complete(sender, currentArg));
        }

        return suggestions.stream()
                .distinct() // Remove duplicates
                .collect(Collectors.toList());
    }

    public void sendUsage(@NotNull CommandSender sender, @NotNull String parentCommand) {
        String commandName = parentCommand + " " + name;
        commandName = commandName.trim();

        if (hasChildren()) {
            List<String> childrenNames = getChildrenNames();
            if (!childrenNames.isEmpty()) {
                sender.sendMessage("§6Usage: §f/" + commandName + " <" + String.join("|", childrenNames) + ">");

                for (CommandNode child : children) {
                    if (!child.isHiddenFromCompletion()) {
                        sender.sendMessage("  §e" + child.name + "§f: " + child.description);
                    }
                }
            }
        } else if (hasTabCompleters()) {
            List<String> typeStrings = tabCompleters.stream()
                    .filter(tc -> !(tc instanceof NodeTabCompleter))
                    .map(TabCompleter::getTypeString)
                    .collect(Collectors.toList());

            if (!typeStrings.isEmpty()) {
                sender.sendMessage("§6Usage: §f/" + commandName + " <" + String.join("|", typeStrings) + ">");
            } else {
                sender.sendMessage("§6Usage: §f/" + commandName + " <value>");
            }
        } else {
            sender.sendMessage("§6Usage: §f/" + commandName);
        }

        if (!description.isEmpty()) {
            sender.sendMessage("§7" + description);
        }
    }

    @Nullable
    public NodeExecutionContext findNodeToExecute(@NotNull String[] args, int argIndex) {
        if (argIndex >= args.length) {
            return new NodeExecutionContext(this, args, argIndex);
        }

        String currentArg = args[argIndex].toLowerCase();

        CommandNode childNode = getChild(currentArg);
        if (childNode == null) {
            return new NodeExecutionContext(this, args, argIndex);
        }

        return childNode.findNodeToExecute(args, argIndex + 1);
    }

    public static class NodeExecutionContext {
        private final CommandNode node;
        private final String[] args;
        private final int consumedArgs;

        public NodeExecutionContext(CommandNode node, String[] args, int consumedArgs) {
            this.node = node;
            this.args = args;
            this.consumedArgs = consumedArgs;
        }

        public CommandNode getNode() {
            return node;
        }

        public String[] getRemainingArgs() {
            if (consumedArgs >= args.length) {
                return new String[0];
            }

            String[] remainingArgs = new String[args.length - consumedArgs];
            System.arraycopy(args, consumedArgs, remainingArgs, 0, remainingArgs.length);
            return remainingArgs;
        }

        public String[] getConsumedArgs() {
            if (consumedArgs == 0) {
                return new String[0];
            }

            String[] consumedArgsArray = new String[consumedArgs];
            System.arraycopy(args, 0, consumedArgsArray, 0, consumedArgs);
            return consumedArgsArray;
        }
    }

    public static class Builder {
        private final String name;
        private String description = "";
        private String permission = "";
        private final List<TabCompleter<?>> tabCompleters = new ArrayList<>();
        private BiFunction<CommandSender, String[], Boolean> executor = null;
        private boolean showInCompletion = true;

        public Builder(@NotNull String name) {
            this.name = name;
        }

        public Builder description(@NotNull String description) {
            this.description = description;
            return this;
        }

        public Builder permission(@NotNull String permission) {
            this.permission = permission;
            return this;
        }

        public Builder tabCompleter(@NotNull TabCompleter<?> tabCompleter) {
            this.tabCompleters.add(tabCompleter);
            return this;
        }

        public Builder tabCompleters(@NotNull List<TabCompleter<?>> tabCompleters) {
            this.tabCompleters.addAll(tabCompleters);
            return this;
        }

        public Builder executor(@Nullable BiFunction<CommandSender, String[], Boolean> executor) {
            this.executor = executor;
            return this;
        }

        public Builder hideFromCompletion() {
            this.showInCompletion = false;
            return this;
        }

        public CommandNode build() {
            return new CommandNode(name, description, permission, tabCompleters, executor, showInCompletion);
        }
    }
}