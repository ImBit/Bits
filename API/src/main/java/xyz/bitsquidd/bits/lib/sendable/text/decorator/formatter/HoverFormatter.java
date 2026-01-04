// HoverFormatter.java
package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

public class HoverFormatter extends AbstractFormatter {
    private final HoverEvent<?> hoverEvent;

    public HoverFormatter(HoverEvent<?> hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    @Override
    public Component format(Component input) {
        return input.hoverEvent(hoverEvent);
    }

}