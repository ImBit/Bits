package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class FontFormatter extends AbstractFormatter {
    public final @NotNull NamespacedKey fontKey;

    public FontFormatter(@NotNull String tag, @NotNull NamespacedKey fontKey) {
        super(tag);
        this.fontKey = fontKey;
    }


    @Override
    public @NotNull Component format(@NotNull Component input) {
        return input.font(fontKey);
    }
}