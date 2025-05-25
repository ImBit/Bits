package xyz.bitsquidd.bits.lib.command;

public enum CommandReturnType {
    SUCCESS(0x77FF00, "#"),
    INFO(0xBBBBFF, "#"),
    ERROR(0xFF2244, "#");

    public final int color;
    public final String icon;


    CommandReturnType(int color, String icon) {
        this.color = color;
        this.icon = icon;
    }
}
