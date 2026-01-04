package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;

public abstract class AbstractFormatter {
    public abstract Component format(Component input);

    public AbstractFormatter createFromData(String data) {
        return this;
    }

}