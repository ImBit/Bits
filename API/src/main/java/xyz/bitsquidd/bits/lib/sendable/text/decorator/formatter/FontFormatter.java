package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

public class FontFormatter extends AbstractFormatter {
    public final Key fontKey;

    public FontFormatter(Key fontKey) {
        this.fontKey = fontKey;
    }

    @Override
    public Component format(Component input) {
        return input.font(fontKey);
    }

}