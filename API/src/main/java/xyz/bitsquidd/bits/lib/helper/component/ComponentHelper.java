package xyz.bitsquidd.bits.lib.helper.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.util.*;

public class ComponentHelper {
    public static @NotNull Component BLANK() {
        return Component.empty()
              .color(NamedTextColor.WHITE)
              .decoration(TextDecoration.OBFUSCATED, false)
              .decoration(TextDecoration.BOLD, false)
              .decoration(TextDecoration.STRIKETHROUGH, false)
              .decoration(TextDecoration.UNDERLINED, false)
              .decoration(TextDecoration.ITALIC, false);
    }

    private static final TextComponent UNSUPPORTED = Component.text("ERROR PARSING", NamedTextColor.RED);

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

    public static List<TextComponent> flatten(final @NotNull Component component) {
        return flatten(component, null);
    }

    // Adapted version of https://gist.github.com/Minikloon/e6a7679d171b90dc4e0731db46d77c84
    public static List<TextComponent> flatten(final @NotNull Component component, final @Nullable Locale locale) {
        final List<TextComponent> flattened = new ArrayList<>();
        final Style style = component.style();
        final Style enforcedState = enforceStyleStates(style);
        final Component first = component.style(enforcedState);

        final Stack<Component> toCheck = new Stack<>();
        toCheck.add(first);

        while (!toCheck.empty()) {
            final TextComponent parent = convertToTextComponent(toCheck.pop(), locale);
            final String content = parent.content();
            if (!content.isEmpty()) flattened.add(parent);

            final List<Component> children = parent.children();
            final List<Component> reversed = children.reversed();
            for (final Component child : reversed) {
                final TextComponent text = convertToTextComponent(child, locale);
                final Style parentStyle = parent.style();
                final Style textStyle = text.style();
                final Style merge = parentStyle.merge(textStyle);
                final TextComponent childComponent = text.style(merge);
                toCheck.add(childComponent);
            }
        }
        return flattened;
    }

    private static @NotNull TextComponent convertToTextComponent(final @NotNull Component component, final @Nullable Locale locale) {
        if (component instanceof TextComponent textComponent) {
            return textComponent;
        } else if (component instanceof TranslatableComponent translatableComponent) {
            return Component.text(getTranslatedString(locale != null ? locale : Locale.getDefault(), translatableComponent));
        } else {
            BitsConfig.getLogger().warn("Unsupported component type: {}", component);
            return UNSUPPORTED;
        }
    }

    private static @NotNull Style enforceStyleStates(final @NotNull Style style) {
        final Style.Builder builder = style.toBuilder();
        final Map<TextDecoration, TextDecoration.State> map = style.decorations();
        map.forEach((decoration, state) -> {
            if (state == TextDecoration.State.NOT_SET) {
                builder.decoration(decoration, false);
            }
        });
        return builder.build();
    }

    public static @NotNull String getTranslatedString(final @NotNull Locale locale, final @NotNull Component component) {
        Component rendered = GlobalTranslator.render(component, locale);

        return PlainTextComponentSerializer.plainText().serialize(rendered);
    }
}
