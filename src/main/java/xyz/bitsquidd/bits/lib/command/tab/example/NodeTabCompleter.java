package xyz.bitsquidd.bits.lib.command.tab.example;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.command.CommandNode;
import xyz.bitsquidd.bits.lib.command.tab.TabCompleter;

import java.util.List;

public class NodeTabCompleter implements TabCompleter<CommandNode> {
    private final CommandNode node;

    public NodeTabCompleter(@NotNull CommandNode node) {
        this.node = node;
    }
    public static NodeTabCompleter of(CommandNode node) {
        return new NodeTabCompleter(node);
    }

    @Override
    public @Nullable CommandNode getValue(String arg) {
        return node.getChild(arg);
    }

    @Override
    public @NotNull String getTypeString() {
        return "CommandNode";
    }

    @Override
    public @NotNull List<String> getPossibleValues(@NotNull CommandSender sender) {
        return node.getChildrenNames();
    }
}