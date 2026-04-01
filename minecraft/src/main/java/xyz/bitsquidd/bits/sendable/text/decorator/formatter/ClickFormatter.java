/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

/**
 * A formatter that applies a {@link ClickEvent} to an Adventure {@link Component}.
 *
 * @since 0.0.10
 */
public class ClickFormatter extends AbstractFormatter {
    public final ClickEvent fontKey;

    public ClickFormatter(ClickEvent clickEvent) {
        this.fontKey = clickEvent;
    }

    @Override
    public Component format(Component input) {
        return input.applyFallbackStyle(fontKey);
    }

}