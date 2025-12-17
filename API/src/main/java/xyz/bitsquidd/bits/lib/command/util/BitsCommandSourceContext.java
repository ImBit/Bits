package xyz.bitsquidd.bits.lib.command.util;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.ApiStatus;

import xyz.bitsquidd.bits.lib.sendable.text.Text;

/**
 * Utility class to encapsulate the CommandSourceStack
 */
public abstract class BitsCommandSourceContext<T> {
    protected final T source;

    public BitsCommandSourceContext(T source) {
        this.source = source;
    }

    public T getSource() {
        return source;
    }

    /**
     * Returns the command sender.
     */
    public abstract <S extends Audience> S getSender();

    /**
     * Sends a message to the command sender.
     * Experimental: this may be expanded in the future to handle command errors and success.
     */
    @ApiStatus.Experimental
    public void respond(Text message) {
        message.send(getSender());
    }

}