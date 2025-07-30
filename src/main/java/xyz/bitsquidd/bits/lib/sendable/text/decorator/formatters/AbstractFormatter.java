package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFormatter {
    public abstract @NotNull Component format(@NotNull Component input);

    public @NotNull AbstractFormatter createFromData(@NotNull String data) {
        return this;
    }
}