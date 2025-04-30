package xyz.bitsquidd.bits.lib.sendable.text.format;

import xyz.bitsquidd.bits.lib.component.color.ColorHelper;

import java.awt.*;

public abstract class TextFormatter {
    private final String prefix;
    private final Color color;

    protected TextFormatter(String prefix, Color color) {
        this.prefix = prefix;
        this.color = color;
    }

    protected Color getSecondaryColor() {
        return ColorHelper.lightenColour(color, 0.5f);
    }


}
