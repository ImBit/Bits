package xyz.bitsquidd.bits.lib.sendable.text.decorator.examples;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class TranslationDecorator extends StyleDecorator {
    @Override
    public @NotNull Component format(@NotNull Component component, @Nullable CommandSender target) {
        if (component instanceof TranslatableComponent translatableComponent) {
            Locale locale = Locale.getDefault();
            if (target instanceof Player player) {
                locale = player.locale();
            }

            component = GlobalTranslator.render(translatableComponent, locale);
        }

        return component;
    }
}
