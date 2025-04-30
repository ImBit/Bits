package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters;

import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

public class StyleFormatter {
    private final String openTag;
    private final String closeTag;
    private final TextDecoration decoration;

    public StyleFormatter(String tag, TextDecoration decoration) {
        this.openTag = "<" + tag + ">";
        this.closeTag = "</" + tag + ">";
        this.decoration = decoration;
    }

    public String getOpenTag() {
        return openTag;
    }

    public String getCloseTag() {
        return closeTag;
    }

    public Style.Builder decorate(Style.Builder style) {
        return style.decorate(decoration);
    }
}