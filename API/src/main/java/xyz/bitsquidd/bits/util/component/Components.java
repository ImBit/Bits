/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.util.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.log.Logger;

import java.util.*;

/**
 * A collection of utilities for working with {@link Component Adventure Components}.
 */
public final class Components {
    private Components() {}

    public static final Component BLANK = Component.empty()
      .applyFallbackStyle(Style.empty()
        .color(NamedTextColor.WHITE)
        .decoration(TextDecoration.OBFUSCATED, false)
        .decoration(TextDecoration.BOLD, false)
        .decoration(TextDecoration.STRIKETHROUGH, false)
        .decoration(TextDecoration.UNDERLINED, false)
        .decoration(TextDecoration.ITALIC, false)
      );

    private static final TextComponent UNSUPPORTED = Component.text("ERROR PARSING", NamedTextColor.RED);

    public static Component styleAll(Component component, Style style) {
        List<Component> children = component.children();
        if (!children.isEmpty()) children.forEach(child -> styleAll(child, style));
        return component.style(style);
    }

    public static List<TextComponent> flatten(final Component component) {
        return flatten(component, null);
    }

    // Adapted version of https://gist.github.com/Minikloon/e6a7679d171b90dc4e0731db46d77c84
    public static List<TextComponent> flatten(final Component component, final @Nullable Locale locale) {
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

    private static TextComponent convertToTextComponent(final Component component, final @Nullable Locale locale) {
        if (component instanceof TextComponent textComponent) {
            return textComponent;
        } else if (component instanceof TranslatableComponent translatableComponent) {
            return Component.text(getTranslatedString(locale != null ? locale : Locale.getDefault(), translatableComponent));
        } else {
            Logger.warn("Unsupported component type: " + component);
            return UNSUPPORTED;
        }
    }

    private static Style enforceStyleStates(final Style style) {
        final Style.Builder builder = style.toBuilder();
        final Map<TextDecoration, TextDecoration.State> map = style.decorations();
        map.forEach((decoration, state) -> {
            if (state == TextDecoration.State.NOT_SET) {
                builder.decoration(decoration, false);
            }
        });
        return builder.build();
    }

    public static String getTranslatedString(final Locale locale, final Component component) {
        Component rendered = GlobalTranslator.render(component, locale);

        return PlainTextComponentSerializer.plainText().serialize(rendered);
    }

}
