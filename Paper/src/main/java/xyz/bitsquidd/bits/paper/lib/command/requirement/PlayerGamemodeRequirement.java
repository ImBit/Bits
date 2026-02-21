/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.lib.command.requirement;

import org.bukkit.GameMode;

import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.paper.lib.command.PaperBitsCommandSourceContext;


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
        return super.test(ctx) && ((PaperBitsCommandSourceContext)(ctx)).requirePlayer().getGameMode().equals(requiredGamemode);
    }

}