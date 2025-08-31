package xyz.bitsquidd.bits.lib.sendable.text.decorator.examples;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.component.ComponentHelper;

public class BlankDecorator extends StyleDecorator {
    @Override
    public @NotNull Component format(@NotNull Component component, @Nullable CommandSender target) {
        return ComponentHelper.styleAll(component, Style.empty());
    }
}
