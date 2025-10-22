package xyz.bitsquidd.bits.lib.sendable.text.decorator;

import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter.AbstractFormatter;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter.StyleFormatter;

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