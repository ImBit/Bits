package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class ClickFormatter extends AbstractFormatter {
    public final @NotNull ClickEvent fontKey;

    public ClickFormatter(@NotNull ClickEvent clickEvent) {
        this.fontKey = clickEvent;
    }

    @Override
    public @NotNull Component format(@NotNull Component input) {
        return input.clickEvent(fontKey);
    }
}