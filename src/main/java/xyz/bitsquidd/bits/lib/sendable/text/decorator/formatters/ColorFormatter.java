package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public class ColorFormatter extends AbstractFormatter {
    public final int color;

    public ColorFormatter(@NotNull String tag, int color) {
        super(tag);
        this.color = color;
    }

    public ColorFormatter(@NotNull String tag, @NotNull TextColor color) {
        this(tag, color.value());
    }

    public ColorFormatter(@NotNull String tag, @NotNull org.bukkit.Color color) {
        this(tag, color.asRGB());
    }

    public ColorFormatter(@NotNull String tag, @NotNull java.awt.Color color) {
        this(tag, color.getRGB());
    }


    @Override
    public @NotNull Component format(@NotNull Component input) {
        return input.color(TextColor.color(color));
    }
}
