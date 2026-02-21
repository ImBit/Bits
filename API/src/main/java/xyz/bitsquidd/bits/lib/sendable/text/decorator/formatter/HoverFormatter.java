/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

public class HoverFormatter extends AbstractFormatter {
    private final HoverEvent<?> hoverEvent;

    public HoverFormatter(HoverEvent<?> hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    @Override
    public Component format(Component input) {
        return input.applyFallbackStyle(hoverEvent);
    }

}