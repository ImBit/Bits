package xyz.bitsquidd.bits.lib.sendable.text.decorator.examples;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.component.ComponentHelper;
import xyz.bitsquidd.bits.lib.component.color.ColorHelper;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.AbstractFormatter;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.ColorFormatter;

public class ColorLightenerFormatter extends AbstractFormatter {
    private static final int FALLBACK_COLOR = 0xFFFFFF;

    private final float lightness;

    public ColorLightenerFormatter(float lightness) {
        this.lightness = lightness;
    }

    @Override
    public @NotNull Component format(@NotNull Component input) {
        TextColor inputColor = input.color();

        return ComponentHelper.styleAll(input, input.style().color(TextColor.color(ColorHelper.lightenColour(inputColor != null ? inputColor.value() : FALLBACK_COLOR, lightness))));
    }
}
