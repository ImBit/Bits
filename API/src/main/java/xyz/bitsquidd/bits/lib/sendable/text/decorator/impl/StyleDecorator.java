package xyz.bitsquidd.bits.lib.sendable.text.decorator.impl;

import xyz.bitsquidd.bits.lib.sendable.text.decorator.AbstractTagDecorator;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter.DynamicColorFormatter;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter.StyleFormatter;

public class StyleDecorator extends AbstractTagDecorator {
    public StyleDecorator() {
        super();
        formatters.put("b", StyleFormatter.bold());
        formatters.put("i", StyleFormatter.italic());
        formatters.put("s", StyleFormatter.strikethrough());
        formatters.put("u", StyleFormatter.underlined());
        formatters.put("o", StyleFormatter.obfuscated());
        formatters.put("c", new DynamicColorFormatter());
    }
}