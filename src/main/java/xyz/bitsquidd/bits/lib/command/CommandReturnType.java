package xyz.bitsquidd.bits.lib.command;

import xyz.bitsquidd.bits.lib.component.color.ColorStore;

public enum CommandReturnType {
    SUCCESS(ColorStore.SUCCESS.color, "#"),
    ERROR(ColorStore.ERROR.color, "#");

    public final int color;
    public final String icon;


    CommandReturnType(int color, String icon) {
        this.color = color;
        this.icon = icon;
    }
}
