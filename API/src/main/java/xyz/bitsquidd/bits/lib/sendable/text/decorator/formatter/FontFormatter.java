package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class FontFormatter extends AbstractFormatter {
    public final @NotNull Key fontKey;

    public FontFormatter(@NotNull Key fontKey) {
        this.fontKey = fontKey;
    }

    @Override
    public @NotNull Component format(@NotNull Component input) {
        return input.font(fontKey);
    }
}