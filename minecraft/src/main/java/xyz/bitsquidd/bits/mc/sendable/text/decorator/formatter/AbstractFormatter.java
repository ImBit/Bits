/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.sendable.text.decorator.formatter;

import net.kyori.adventure.text.Component;

/**
 * An abstract base class for formatters that apply specific styles to Adventure {@link Component}s.
 *
 * @since 0.0.10
 */
public abstract class AbstractFormatter {
    /**
     * Applies the formatting to the input component.
     *
     * @param input the component to format
     *
     * @return the formatted component
     *
     * @since 0.0.10
     */
    public abstract Component format(Component input);

    /**
     * Creates a new instance of this formatter using provided data, which may be empty.
     *
     * @param data the data used to configure the new instance
     *
     * @return a configured formatter instance
     *
     * @since 0.0.10
     */
    public AbstractFormatter createFromData(String data) {
        return this;
    }

}