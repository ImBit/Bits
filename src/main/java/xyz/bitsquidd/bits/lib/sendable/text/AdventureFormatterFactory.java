package xyz.bitsquidd.bits.lib.sendable.text;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.*;

import java.util.ArrayList;
import java.util.List;

public class AdventureFormatterFactory {

    public static @NotNull List<AbstractFormatter> fromStyle(@NotNull Style style) {
        List<AbstractFormatter> formatters = new ArrayList<>();

        TextColor color = style.color();
        if (color != null) {
            formatters.add(new BasicColorFormatter(color));
        }

        for (TextDecoration decoration : TextDecoration.values()) {
            if (style.decoration(decoration) == TextDecoration.State.TRUE) {
                formatters.add(new StyleFormatter(decoration));
            }
        }

        HoverEvent<?> hoverEvent = style.hoverEvent();
        if (hoverEvent != null) {
            formatters.add(new HoverFormatter(hoverEvent));
        }

        ClickEvent clickEvent = style.clickEvent();
        if (clickEvent != null) {
            formatters.add(new ClickFormatter(clickEvent));
        }

        Key font = style.font();
        if (font != null) {
            formatters.add(new FontFormatter(font));
        }

        return formatters;
    }
}