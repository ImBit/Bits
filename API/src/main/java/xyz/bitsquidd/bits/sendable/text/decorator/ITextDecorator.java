/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.sendable.text.decorator;

import net.kyori.adventure.text.Component;

import java.util.Locale;

public interface ITextDecorator {
    Component format(Component component, Locale locale);

}
