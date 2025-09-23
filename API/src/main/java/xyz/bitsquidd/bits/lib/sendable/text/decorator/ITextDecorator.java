package xyz.bitsquidd.bits.lib.sendable.text.decorator;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ITextDecorator {
    @NotNull Component format(@NotNull Component component, @Nullable Audience target);
}
