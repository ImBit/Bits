/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;

public abstract class AbstractFormatter {
    public abstract Component format(Component input);

    public AbstractFormatter createFromData(String data) {
        return this;
    }

}