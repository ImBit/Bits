package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public class ColorFormatter extends AbstractFormatter {
    public final int color;

    public ColorFormatter(int color) {
        this.color = color;
    }

    public ColorFormatter(@NotNull TextColor color) {
        this(color.value());
    }

    public ColorFormatter(@NotNull org.bukkit.Color color) {
        this(color.asRGB());
    }

    public ColorFormatter(@NotNull java.awt.Color color) {
        this(color.getRGB());
    }


    @Override
    public @NotNull Component format(@NotNull Component input) {
        return input.color(TextColor.color(color));
    }
}
