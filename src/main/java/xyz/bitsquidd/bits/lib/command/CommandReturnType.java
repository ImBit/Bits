package xyz.bitsquidd.bits.lib.command;

import org.jetbrains.annotations.NotNull;

public enum CommandReturnType {
    SUCCESS(0x77FF00, "#"),
    INFO(0xBBBBFF, "#"),
    ERROR(0xFF2244, "#");

    public final int color;
    public final String icon;


    CommandReturnType(int color, @NotNull String icon) {
        this.color = color;
        this.icon = icon;
    }
}
