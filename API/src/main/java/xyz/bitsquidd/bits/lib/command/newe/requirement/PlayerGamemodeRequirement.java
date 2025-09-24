package xyz.bitsquidd.bits.lib.command.newe.requirement;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PlayerGamemodeRequirement extends BitsRequirement {
    public static final PlayerGamemodeRequirement CREATIVE = new PlayerGamemodeRequirement(GameMode.CREATIVE);
    public static final PlayerGamemodeRequirement SURVIVAL = new PlayerGamemodeRequirement(GameMode.SURVIVAL);
    public static final PlayerGamemodeRequirement ADVENTURE = new PlayerGamemodeRequirement(GameMode.ADVENTURE);
    public static final PlayerGamemodeRequirement SPECTATOR = new PlayerGamemodeRequirement(GameMode.SPECTATOR);

    private final GameMode requiredGamemode;

    public PlayerGamemodeRequirement(GameMode requiredGamemode) {
        this.requiredGamemode = requiredGamemode;
    }

    @Override
    public boolean test(CommandSourceStack commandSourceStack) {
        CommandSender sender = commandSourceStack.getSender();

        if (!(sender instanceof Player player)) {
            return fail(commandSourceStack, Component.text("You must be a player and in gamemode " + requiredGamemode + " to run this command."));
        }

        if (player.getGameMode() != requiredGamemode) {
            return fail(commandSourceStack, Component.text("You must be in gamemode " + requiredGamemode + " to run this command."));
        }

        return true;
    }
}
