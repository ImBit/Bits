/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command.debugging;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreeDebugger<T> {
    public String visualizeCommandTree(List<LiteralCommandNode<T>> nodes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Command Tree:\n");

        for (LiteralCommandNode<T> node : nodes) {
            sb.append("root\n");
            visualizeNode(node, "", true, sb);
        }

        return sb.toString();
    }

    private void visualizeNode(CommandNode<T> node, String prefix, boolean isLast, StringBuilder sb) {
        String nodeName = getNodeName(node);
        String connector = isLast ? "└── " : "├── ";
        sb.append(prefix).append(connector).append(nodeName);

        if (node.getCommand() != null) {
            sb.append(" [executable]");
        }

        sb.append("\n");

        Collection<CommandNode<T>> children = node.getChildren();
        if (children.isEmpty()) {
            return;
        }

        String childPrefix = prefix + (isLast ? "    " : "│   ");

        List<CommandNode<T>> childList = new ArrayList<>(children);
        for (int i = 0; i < childList.size(); i++) {
            boolean isLastChild = (i == childList.size() - 1);
            visualizeNode(childList.get(i), childPrefix, isLastChild, sb);
        }
    }

    @SuppressWarnings("rawtypes")
    private String getNodeName(CommandNode<T> node) {
        if (node instanceof LiteralCommandNode) {
            return ((LiteralCommandNode<T>)node).getLiteral();
        } else if (node instanceof com.mojang.brigadier.tree.ArgumentCommandNode argumentCommandNode) {
            return "<" + argumentCommandNode.getType().toString() + ">";
        } else {
            return node.getName();
        }
    }

}
