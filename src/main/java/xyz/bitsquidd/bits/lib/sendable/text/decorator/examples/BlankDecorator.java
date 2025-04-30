package xyz.bitsquidd.bits.lib.sendable.text.decorator.examples;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.component.ComponentHelper;

public class BlankDecorator extends TextTagDecorator {
    @Override
    public int getColor() {
        return 0xFFFFFF;
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public @NotNull Component format(Component component, CommandSender target) {
        return ComponentHelper.styleAll(component, Style.empty());
    }
}
