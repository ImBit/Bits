package xyz.bitsquidd.bits.lib.sendable.text.decorator.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.helper.component.ComponentHelper;

import java.util.Locale;

public class BlankDecorator extends StyleDecorator {
    @Override
    public @NotNull Component format(@NotNull Component component, @NotNull Locale locale) {
        return ComponentHelper.styleAll(component, Style.empty());
    }
}
