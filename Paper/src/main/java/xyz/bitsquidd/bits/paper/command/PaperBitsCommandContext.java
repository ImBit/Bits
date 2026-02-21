/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.bitsquidd.bits.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.BitsConfig;

public class PaperBitsCommandContext extends BitsCommandContext<CommandSourceStack> {
    public PaperBitsCommandContext(CommandContext<CommandSourceStack> brigadierContext) {
        super(brigadierContext);
    }

    @Override
    protected BitsCommandSourceContext<CommandSourceStack> createSourceContext(CommandContext<CommandSourceStack> brigadierContext) {
        return ((PaperBitsCommandManager)BitsConfig.get().getCommandManager()).createSourceContext(brigadierContext.getSource());
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandSender getSender() {
        return source.getSender();
    }

    public Player requirePlayer() {
        if (!(getSender() instanceof Player player)) throw new IllegalStateException("Command sender must be a player");
        return player;
    }

    public Location getLocation() {
        return source.getSource().getLocation();
    }

}
