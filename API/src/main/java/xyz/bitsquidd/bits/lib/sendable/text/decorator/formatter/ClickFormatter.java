package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

public class ClickFormatter extends AbstractFormatter {
    public final ClickEvent fontKey;

    public ClickFormatter(ClickEvent clickEvent) {
        this.fontKey = clickEvent;
    }

    @Override
    public Component format(Component input) {
        return input.clickEvent(fontKey);
    }

}