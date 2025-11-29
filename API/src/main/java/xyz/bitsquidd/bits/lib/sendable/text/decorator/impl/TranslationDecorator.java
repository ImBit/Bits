package xyz.bitsquidd.bits.lib.sendable.text.decorator.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.GlobalTranslator;

import java.util.Locale;

public class TranslationDecorator extends StyleDecorator {

    @Override
    public Component format(Component component, Locale locale) {
        if (component instanceof TranslatableComponent translatableComponent) {
            component = GlobalTranslator.render(translatableComponent, locale);
        }

        return component;
    }

}
