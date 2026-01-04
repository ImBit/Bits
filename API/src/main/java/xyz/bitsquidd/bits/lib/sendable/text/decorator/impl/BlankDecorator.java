package xyz.bitsquidd.bits.lib.sendable.text.decorator.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;

import xyz.bitsquidd.bits.lib.helper.component.Components;

import java.util.Locale;

public class BlankDecorator extends StyleDecorator {
    @Override
    public Component format(Component component, Locale locale) {
        return Components.styleAll(component, Style.empty());
    }

}
