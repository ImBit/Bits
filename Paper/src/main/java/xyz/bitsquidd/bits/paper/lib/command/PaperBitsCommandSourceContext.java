package xyz.bitsquidd.bits.paper.lib.command;

import net.kyori.adventure.audience.Audience;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;

public class PaperBitsCommandSourceContext extends BitsCommandSourceContext<CommandSourceStack> {
    public PaperBitsCommandSourceContext(CommandSourceStack source) {
        super(source);
    }

    @Override
    public Audience getSender() {
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

}
