package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

public class StyleFormatter extends AbstractFormatter {
    public final @NotNull TextDecoration decoration;

    public StyleFormatter(@NotNull TextDecoration decoration) {
        this.decoration = decoration;
    }

    @Override
    public @NotNull Component format(@NotNull Component input) {
        return input.decorate(decoration);
    }
}