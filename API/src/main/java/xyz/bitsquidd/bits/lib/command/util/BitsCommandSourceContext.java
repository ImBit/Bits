package xyz.bitsquidd.bits.lib.command.util;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import xyz.bitsquidd.bits.lib.sendable.text.Text;

/**
 * Utility class to encapsulate the CommandSourceStack
 */
public abstract class BitsCommandSourceContext<T> {
    private final T source;

    public BitsCommandSourceContext(T source) {
        this.source = source;
    }

    /**
     * Returns the command sender.
     */
    public CommandSender getSender() {
        return source.getSender();
    }

    /**
     * Returns a non-null player if the command sender is a player, otherwise throws an exception.
     */
    public Player requirePlayer() {
        if (!(getSender() instanceof Player player)) throw new IllegalStateException("Command sender must be a player");
        return player;
    }

    /**
     * Returns the location this command was executed at.
     */
    public Location getLocation() {
        return source.getLocation();
    }

    /**
     * Sends a message to the command sender.
     * Experimental: this may be expanded in the future to handle command errors and success.
     */
    @ApiStatus.Experimental
    public void respond(Text message) {
        message.send(getSender());
    }

}