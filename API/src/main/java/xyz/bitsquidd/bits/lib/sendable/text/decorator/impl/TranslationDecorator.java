package xyz.bitsquidd.bits.lib.sendable.text.decorator.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class TranslationDecorator extends StyleDecorator {
    @Override
    public Component format(Component component, Player target) {
        if (component instanceof TranslatableComponent translatableComponent) {
            Locale locale;
            if (target instanceof Player player) {
                locale = player.locale();
            } else {
                locale = Locale.getDefault();
            }

            component = GlobalTranslator.render(translatableComponent, locale);
        }

        return component;
    }
}
