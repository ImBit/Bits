package xyz.bitsquidd.bits.lib.component.color;

import net.kyori.adventure.text.format.TextColor;

public enum ColorStore {
    DEBUG(0xBBBBFF),
    INFO(0x11DDFF),
    SUCCESS(0x77FF00),
    WARNING(0xFFB22B),
    ERROR(0xFF2244),
    CRITICAL(0x880044),

    ;

    public final int color;

    ColorStore(int color) {
        this.color = color;
    }

    public TextColor getTextColor() {
        return TextColor.color(color);
    }
}
