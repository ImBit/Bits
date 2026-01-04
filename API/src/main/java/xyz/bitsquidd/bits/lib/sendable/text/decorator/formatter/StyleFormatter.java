package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

public class StyleFormatter extends AbstractFormatter {
    private final Style style;

    public StyleFormatter(Style style) {
        this.style = style;
    }

    @Override
    public Component format(Component input) {
        return input.style(input.style().merge(style));
    }

    public static StyleFormatter bold() {
        return new StyleFormatter(Style.style().decoration(TextDecoration.BOLD, true).build());
    }

    public static StyleFormatter italic() {
        return new StyleFormatter(Style.style().decoration(TextDecoration.ITALIC, true).build());
    }

    public static StyleFormatter strikethrough() {
        return new StyleFormatter(Style.style().decoration(TextDecoration.STRIKETHROUGH, true).build());
    }

    public static StyleFormatter underlined() {
        return new StyleFormatter(Style.style().decoration(TextDecoration.UNDERLINED, true).build());
    }

    public static StyleFormatter obfuscated() {
        return new StyleFormatter(Style.style().decoration(TextDecoration.OBFUSCATED, true).build());
    }

}