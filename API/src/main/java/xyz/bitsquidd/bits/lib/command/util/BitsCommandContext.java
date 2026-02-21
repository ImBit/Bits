/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.command.util;

import com.mojang.brigadier.context.CommandContext;
import net.kyori.adventure.audience.Audience;

import xyz.bitsquidd.bits.lib.sendable.text.Text;

/**
 * Utility class to encapsulate command context
 */
public abstract class BitsCommandContext<T> {
    protected final CommandContext<T> brigadierContext;
    protected final BitsCommandSourceContext<T> source;

    public BitsCommandContext(CommandContext<T> brigadierContext) {
        this.brigadierContext = brigadierContext;
        this.source = createSourceContext(brigadierContext);
    }

    protected abstract BitsCommandSourceContext<T> createSourceContext(CommandContext<T> brigadierContext);


    /**
     * Returns the Brigadier {@link T}.
     */
    public BitsCommandSourceContext<T> getSource() {
        return source;
    }

    public CommandContext<T> getBrigadierContext() {
        return brigadierContext;
    }

    public abstract <S extends Audience> S getSender();

    public final void respond(Text message) {
        message.send(getSender());
    }

    public String getValueAtIndex(int index) {
        try {
            String[] parts = getFullInput().split(" ");
            return parts[index];
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public String getLastInput() {
        String input = getFullInput();
        String[] parts = input.split(" ");
        if (parts.length == 0) return "";
        return parts[parts.length - 1];
    }

    public String getFullInput() {
        return brigadierContext.getInput();
    }

}