/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import xyz.bitsquidd.bits.util.color.Colors;

/**
 * A formatter that dynamically resolves colors from tag data, supporting hex codes and named colors.
 *
 * @since 0.0.10
 */
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
                color = TextColor.color(Colors.fromName(data));
            }
        } catch (Exception ignored) {}

        return color != null ? new BasicColorFormatter(color) : new BasicColorFormatter(NamedTextColor.WHITE);
    }

}