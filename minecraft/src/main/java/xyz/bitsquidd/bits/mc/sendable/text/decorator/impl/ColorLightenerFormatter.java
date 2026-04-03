/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.sendable.text.decorator.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import xyz.bitsquidd.bits.mc.sendable.text.decorator.formatter.AbstractFormatter;
import xyz.bitsquidd.bits.util.color.Colors;
import xyz.bitsquidd.bits.util.component.Components;

/**
 * A formatter that lightens the existing color of an Adventure {@link Component}.
 *
 * @since 0.0.10
 */
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
