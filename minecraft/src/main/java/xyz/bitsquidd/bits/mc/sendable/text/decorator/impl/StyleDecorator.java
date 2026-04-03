/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.sendable.text.decorator.impl;

import xyz.bitsquidd.bits.mc.sendable.text.decorator.AbstractTagDecorator;
import xyz.bitsquidd.bits.mc.sendable.text.decorator.formatter.DynamicColorFormatter;
import xyz.bitsquidd.bits.mc.sendable.text.decorator.formatter.StyleFormatter;

/**
 * A tag-based decorator that provides standard formatting operations like bold, italic, and color.
 * <p>
 * This decorator recognizes the following tags:
 * <ul>
 *     <li>{@code <bTag>} / {@code <b>} - Bold</li>
 *     <li>{@code <iTag>} / {@code <i>} - Italic</li>
 *     <li>{@code <sTag>} / {@code <s>} - Strikethrough</li>
 *     <li>{@code <uTag>} / {@code <u>} - Underlined</li>
 *     <li>{@code <oTag>} / {@code <o>} - Obfuscated</li>
 *     <li>{@code <c:color>} - Color (hex or named)</li>
 * </ul>
 *
 * @since 0.0.10
 */
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