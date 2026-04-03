/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.sendable.text.decorator.formatter;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

/**
 * A formatter that applies a custom {@link Key font} to an Adventure {@link Component}.
 *
 * @since 0.0.10
 */
public class FontFormatter extends AbstractFormatter {
    public final Key fontKey;

    public FontFormatter(Key fontKey) {
        this.fontKey = fontKey;
    }

    @Override
    public Component format(Component input) {
        return input.font(fontKey);
    }

}