package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

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


    @Override
    public Component format(Component input) {
        return input.applyFallbackStyle(TextColor.color(color));
    }

}
