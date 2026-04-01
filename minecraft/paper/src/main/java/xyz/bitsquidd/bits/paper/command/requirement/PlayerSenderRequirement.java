/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.command.requirement;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.sendable.text.Text;

public class PlayerSenderRequirement extends BitsCommandRequirement {
    public static final PlayerSenderRequirement INSTANCE = new PlayerSenderRequirement();

    protected PlayerSenderRequirement() {}

    @Override
    public boolean test(BitsCommandSourceContext<?> ctx) {
        return ctx.getSender() instanceof Player;
    }

    @Override
    public @Nullable Text getFailureMessage(BitsCommandSourceContext<?> ctx) {
        return Text.of(Component.text("This command can only be executed by a player."));
    }

}