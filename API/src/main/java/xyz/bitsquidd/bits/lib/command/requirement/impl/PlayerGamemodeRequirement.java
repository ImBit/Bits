package xyz.bitsquidd.bits.lib.command.requirement.impl;

import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.info.BitsCommandContext;


public class PlayerGamemodeRequirement extends PlayerSenderRequirement {
    public static final PlayerGamemodeRequirement CREATIVE = new PlayerGamemodeRequirement(GameMode.CREATIVE);
    public static final PlayerGamemodeRequirement SURVIVAL = new PlayerGamemodeRequirement(GameMode.SURVIVAL);
    public static final PlayerGamemodeRequirement ADVENTURE = new PlayerGamemodeRequirement(GameMode.ADVENTURE);
    public static final PlayerGamemodeRequirement SPECTATOR = new PlayerGamemodeRequirement(GameMode.SPECTATOR);

    private final GameMode requiredGamemode;

    public PlayerGamemodeRequirement(GameMode requiredGamemode) {
        this.requiredGamemode = requiredGamemode;
    }

    @Override
    public boolean test(@NotNull BitsCommandContext context) {
        return super.test(context) && context.requirePlayer().getGameMode().equals(requiredGamemode);
    }
}