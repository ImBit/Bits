package xyz.bitsquidd.bits.lib.sendable.text.decorator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.component.ComponentHelper;
import xyz.bitsquidd.bits.lib.component.color.ColorHelper;

public interface ITextDecorator {
    int getColor();
    String getPrefix();

    default int getSecondaryColor() {
        return ColorHelper.lightenColour(getColor(), 0.5f);
    }

    default Style getStyle() {
        return Style.style(TextColor.color(getColor()));
    }

    default @NotNull Component format(Component component, CommandSender target) {
        String componentContent = ComponentHelper.getContent(component, target instanceof Player player ? player.locale() : null);

        return Component.empty()
                .append(Component.text(getPrefix()+" "))
                .append(ComponentHelper.styleAll(component, getStyle()));
    }
}
