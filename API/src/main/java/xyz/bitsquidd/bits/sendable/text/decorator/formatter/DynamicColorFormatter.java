/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class DynamicColorFormatter extends AbstractFormatter {
    @Override
    public Component format(Component input) {
        return input;
    }

    @Override
    public AbstractFormatter createFromData(String data) {
        TextColor color = null;

        try {
            if (data.matches("[0-9a-fA-F]{6}")) {
                color = TextColor.color(Integer.parseInt(data, 16));
            } else if (!data.isEmpty()) {
                color = parseNamedColor(data);
            }
        } catch (Exception ignored) {}

        return color != null ? new BasicColorFormatter(color) : new BasicColorFormatter(NamedTextColor.WHITE);
    }

    private TextColor parseNamedColor(String colorName) {
        return switch (colorName.toLowerCase()) {
            case "red" -> NamedTextColor.RED;
            case "green" -> NamedTextColor.GREEN;
            case "blue" -> NamedTextColor.BLUE;
            case "yellow" -> NamedTextColor.YELLOW;
            case "white" -> NamedTextColor.WHITE;
            case "black" -> NamedTextColor.BLACK;
            case "gray", "grey" -> NamedTextColor.GRAY;
            case "purple" -> NamedTextColor.LIGHT_PURPLE;
            case "orange" -> NamedTextColor.GOLD;
            case "pink" -> NamedTextColor.LIGHT_PURPLE;
            default -> null;
        };
    }

}