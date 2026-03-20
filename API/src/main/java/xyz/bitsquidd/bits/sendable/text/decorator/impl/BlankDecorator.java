/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.sendable.text.decorator.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;

import xyz.bitsquidd.bits.util.component.Components;

import java.util.Locale;

/**
 * A decorator that strips all styling from a {@link Component}.
 *
 * @since 0.0.10
 */
public class BlankDecorator extends StyleDecorator {
    @Override
    public Component format(Component component, Locale locale) {
        return Components.styleAll(component, Style.empty());
    }

}
