/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

/**
 * A formatter that applies a constant color to an Adventure {@link Component}.
 *
 * @since 0.0.10
 */
public class BasicColorFormatter extends AbstractFormatter {
    public final int color;

    public BasicColorFormatter(int color) {
        this.color = color;
    }

    public BasicColorFormatter(TextColor color) {
        this(color.value());
    }

    public BasicColorFormatter(java.awt.Color color) {
        this(color.getRGB());
    }


    /**
     * Applies the constant color as a fallback style to the input component.
     *
     * @param input the component to style
     *
     * @return the styled component
     *
     * @since 0.0.10
     */
    @Override
    public Component format(Component input) {
        return input.applyFallbackStyle(TextColor.color(color));
    }

}
