/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.sendable.text.decorator.formatter;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

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