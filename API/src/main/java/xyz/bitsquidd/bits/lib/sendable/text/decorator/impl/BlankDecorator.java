package xyz.bitsquidd.bits.lib.sendable.text.decorator.impl;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.helper.component.ComponentHelper;

public class BlankDecorator extends StyleDecorator {
    @Override
    public @NotNull Component format(@NotNull Component component, @Nullable Audience target) {
        return ComponentHelper.styleAll(component, Style.empty());
    }
}
