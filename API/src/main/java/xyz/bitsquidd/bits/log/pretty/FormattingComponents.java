/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.log.pretty;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class FormattingComponents {
    private final @Nullable ANSI.Foreground foreground;
    private final @Nullable ANSI.Background background;
    private final @Nullable List<ANSI.Style> styles;

    private FormattingComponents(@Nullable ANSI.Foreground foreground, @Nullable ANSI.Background background, @Nullable List<ANSI.Style> styles) {
        this.foreground = foreground;
        this.background = background;
        this.styles = styles;
    }

    public static FormattingComponents of(@Nullable ANSI.Foreground foreground, @Nullable ANSI.Background background, @Nullable List<ANSI.Style> styles) {
        return new FormattingComponents(foreground, background, styles);
    }

    public String format(String input) {
        StringBuilder builder = new StringBuilder();

        if (styles != null) {
            for (ANSI.Style style : styles) {
                builder.append(style.code);
            }
        }

        if (foreground != null) builder.append(foreground.code);
        if (background != null) builder.append(background.code);

        builder.append(input);
        builder.append(ANSI.Style.RESET.code);
        return builder.toString();
    }

    public @Nullable ANSI.Foreground getForeground() {
        return foreground;
    }

    public @Nullable ANSI.Background getBackground() {
        return background;
    }

    public @Nullable List<ANSI.Style> getStyles() {
        return styles;
    }

}
