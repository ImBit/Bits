/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.sendable.text.decorator;

import net.kyori.adventure.text.Component;

import java.util.Locale;

/**
 * Represents a decorator that can modify or format an Adventure {@link Component}.
 * <p>
 * Decorators are used to apply consistent styling, translations, or custom tag processing
 * to text components before they are sent to an audience.
 *
 * @since 0.0.10
 */
public interface ITextDecorator {
    /**
     * Applies formatting to the specified component based on the provided locale.
     *
     * @param component the component to format
     * @param locale    the locale to use for translations or localized formatting
     *
     * @return the formatted component
     *
     * @since 0.0.10
     */
    Component format(Component component, Locale locale);

}
