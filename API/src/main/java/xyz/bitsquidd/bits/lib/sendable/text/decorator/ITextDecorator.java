package xyz.bitsquidd.bits.lib.sendable.text.decorator;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public interface ITextDecorator {
    @NotNull Component format(@NotNull Component component, @NotNull Locale locale);
}
