package xyz.bitsquidd.bits.lib.sendable.text;

import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.AbstractFormatter;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.StyleFormatter;

import java.util.ArrayList;
import java.util.List;

public class AdventureFormatterFactory {

    public static @NotNull List<AbstractFormatter> fromStyle(@NotNull Style style) {
        List<AbstractFormatter> formatters = new ArrayList<>();

        if (!style.isEmpty()) {
            formatters.add(new StyleFormatter(style));
        }

        return formatters;
    }
}