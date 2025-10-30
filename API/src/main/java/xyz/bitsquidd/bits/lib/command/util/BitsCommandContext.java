package xyz.bitsquidd.bits.lib.command.util;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.sendable.text.Text;

/**
 * Utility class to encapsulate command context
 */
@NullMarked
public class BitsCommandContext {
    private final CommandContext<CommandSourceStack> brigadierContext;
    private final BitsCommandSourceContext source;

    public BitsCommandContext(CommandContext<CommandSourceStack> brigadierContext) {
        this.brigadierContext = brigadierContext;
        this.source = new BitsCommandSourceContext(brigadierContext.getSource());
    }


    /**
     * Returns the Brigadier {@link CommandSourceStack}.
     */
    public BitsCommandSourceContext getSource() {
        return source;
    }

    public CommandContext<CommandSourceStack> getBrigadierContext() {
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