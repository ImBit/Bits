package xyz.bitsquidd.bits.lib.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class ComponentHelper {

    public static Stream<Component> getChildren(Component component) {
        return component.children().stream();
    }

    public static Component styleAll(Component component, Style style) {
        List<Component> children = component.children();

        if (children.isEmpty()) {
            return component.style(style);
        } else {
            for (Component child : children) {
                styleAll(child, style);
            }

            return component.style(style);
        }
    }

    public static String getContent(@NotNull Component component, @Nullable Locale locale) {
        Component rendered = component;
        if (locale != null) {
            rendered = GlobalTranslator.render(component, locale);
        }
        return PlainTextComponentSerializer.plainText().serialize(rendered);

    }
}
