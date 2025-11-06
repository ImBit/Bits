package xyz.bitsquidd.bits.lib.sendable.text.decorator;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ITextDecorator {
    @NotNull Component format(@NotNull Component component, @NotNull Player target);
}
