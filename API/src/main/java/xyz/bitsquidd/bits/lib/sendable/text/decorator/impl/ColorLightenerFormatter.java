package xyz.bitsquidd.bits.lib.sendable.text.decorator.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.helper.color.ColorHelper;
import xyz.bitsquidd.bits.lib.helper.component.ComponentHelper;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter.AbstractFormatter;

public class ColorLightenerFormatter extends AbstractFormatter {
    private static final int FALLBACK_COLOR = 0xFFFFFF;

    private final float lightness;

    public ColorLightenerFormatter(float lightness) {
        this.lightness = lightness;
    }

    @Override
    public Component format(Component input) {
        TextColor inputColor = input.color();

        return ComponentHelper.styleAll(input, input.style().color(TextColor.color(ColorHelper.lightenColour(inputColor != null ? inputColor.value() : FALLBACK_COLOR, lightness))));
    }
}
