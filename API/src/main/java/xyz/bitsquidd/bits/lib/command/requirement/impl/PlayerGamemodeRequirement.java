package xyz.bitsquidd.bits.lib.command.requirement.impl;

import org.bukkit.GameMode;

import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;


public class PlayerGamemodeRequirement extends PlayerSenderRequirement {
    public static final PlayerGamemodeRequirement CREATIVE = new PlayerGamemodeRequirement(GameMode.CREATIVE);
    public static final PlayerGamemodeRequirement SURVIVAL = new PlayerGamemodeRequirement(GameMode.SURVIVAL);
    public static final PlayerGamemodeRequirement ADVENTURE = new PlayerGamemodeRequirement(GameMode.ADVENTURE);
    public static final PlayerGamemodeRequirement SPECTATOR = new PlayerGamemodeRequirement(GameMode.SPECTATOR);

    private final GameMode requiredGamemode;

    private PlayerGamemodeRequirement(GameMode requiredGamemode) {
        super();
        this.requiredGamemode = requiredGamemode;
    }

    @Override
    public boolean test(BitsCommandSourceContext<?> ctx) {
        return super.test(ctx) && ctx.requirePlayer().getGameMode().equals(requiredGamemode);
    }
}