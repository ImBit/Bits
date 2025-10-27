package xyz.bitsquidd.bits.lib.command.debugging;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreeDebugger {
    public static String visualizeCommandTree(List<LiteralCommandNode<CommandSourceStack>> nodes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Command Tree:\n");

        for (LiteralCommandNode<CommandSourceStack> node : nodes) {
            sb.append("root\n");
            visualizeNode(node, "", true, sb);
        }

        return sb.toString();
    }

    private static void visualizeNode(CommandNode<CommandSourceStack> node, String prefix, boolean isLast, StringBuilder sb) {
        String nodeName = getNodeName(node);
        String connector = isLast ? "└── " : "├── ";
        sb.append(prefix).append(connector).append(nodeName);

        if (node.getCommand() != null) {
            sb.append(" [executable]");
        }

        sb.append("\n");

        Collection<CommandNode<CommandSourceStack>> children = node.getChildren();
        if (children.isEmpty()) {
            return;
        }

        String childPrefix = prefix + (isLast ? "    " : "│   ");

        List<CommandNode<CommandSourceStack>> childList = new ArrayList<>(children);
        for (int i = 0; i < childList.size(); i++) {
            boolean isLastChild = (i == childList.size() - 1);
            visualizeNode(childList.get(i), childPrefix, isLastChild, sb);
        }
    }

    private static String getNodeName(CommandNode<CommandSourceStack> node) {
        if (node instanceof LiteralCommandNode) {
            return ((LiteralCommandNode<CommandSourceStack>)node).getLiteral();
        } else if (node instanceof com.mojang.brigadier.tree.ArgumentCommandNode argumentCommandNode) {
            return "<" + argumentCommandNode.getType().toString() + ">";
        } else {
            return node.getName();
        }
    }
}
