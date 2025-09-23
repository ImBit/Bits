// HoverFormatter.java
package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

public class HoverFormatter extends AbstractFormatter {
    private final @NotNull HoverEvent<?> hoverEvent;

    public HoverFormatter(@NotNull HoverEvent<?> hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    @Override
    public @NotNull Component format(@NotNull Component input) {
        return input.hoverEvent(hoverEvent);
    }
}