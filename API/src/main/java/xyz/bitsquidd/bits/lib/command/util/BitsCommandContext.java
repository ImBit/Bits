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

    public final Audience getSender() {
        return source.getSender();
    }

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