package xyz.bitsquidd.bits.lib.sendable.text.decorator.examples;

import net.kyori.adventure.text.format.TextDecoration;

import xyz.bitsquidd.bits.lib.sendable.text.decorator.AbstractTagDecorator;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.DynamicColorFormatter;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.StyleFormatter;

public class StyleDecorator extends AbstractTagDecorator {
    public StyleDecorator() {
        super();
        formatters.put("b", new StyleFormatter(TextDecoration.BOLD));
        formatters.put("i", new StyleFormatter(TextDecoration.ITALIC));
        formatters.put("s", new StyleFormatter(TextDecoration.STRIKETHROUGH));
        formatters.put("u", new StyleFormatter(TextDecoration.UNDERLINED));
        formatters.put("o", new StyleFormatter(TextDecoration.OBFUSCATED));
        formatters.put("c", new DynamicColorFormatter());
    }
}