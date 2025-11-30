package xyz.bitsquidd.bits.lib.command.util;

import com.mojang.brigadier.context.CommandContext;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

/**
 * Utility class to encapsulate command context
 */
public abstract class BitsCommandContext<T> {
    private final CommandContext<T> brigadierContext;
    private final BitsCommandSourceContext<T> source;

    public BitsCommandContext(CommandContext<T> brigadierContext) {
        this.brigadierContext = brigadierContext;
        this.source = BitsConfig.get().getCommandManager().createSourceContext(brigadierContext.getSource());
    }


    /**
     * Returns the Brigadier {@link T}.
     */
    public BitsCommandSourceContext getSource() {
        return source;
    }

    public CommandContext<T> getBrigadierContext() {
        return brigadierContext;
    }

    public CommandSender getSender() {
        return source.getSender();
    }

    public Player requirePlayer() {
        if (!(getSender() instanceof Player player)) throw new IllegalStateException("Command sender must be a player");
        return player;
    }

    public Location getLocation() {
        return source.getLocation();
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