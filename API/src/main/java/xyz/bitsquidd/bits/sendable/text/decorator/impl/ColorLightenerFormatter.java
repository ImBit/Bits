/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.sendable.text.decorator.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import xyz.bitsquidd.bits.helper.color.Colors;
import xyz.bitsquidd.bits.helper.component.Components;
import xyz.bitsquidd.bits.sendable.text.decorator.formatter.AbstractFormatter;

public class ColorLightenerFormatter extends AbstractFormatter {
    private static final int FALLBACK_COLOR = 0xFFFFFF;

    private final float lightness;

    public ColorLightenerFormatter(float lightness) {
        this.lightness = lightness;
    }

    @Override
    public Component format(Component input) {
        TextColor inputColor = input.color();

        return Components.styleAll(input, input.style().color(TextColor.color(Colors.lightenColour(inputColor != null ? inputColor.value() : FALLBACK_COLOR, lightness))));
    }

}
