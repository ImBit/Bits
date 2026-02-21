/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.lib.command;

import net.minecraft.commands.CommandSourceStack;

import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.requirement.BitsRequirementRegistry;
import xyz.bitsquidd.bits.paper.lib.command.requirement.ConsoleSenderRequirement;
import xyz.bitsquidd.bits.paper.lib.command.requirement.PlayerSenderRequirement;

import java.util.Map;

public class PaperBitsRequirementRegistry extends BitsRequirementRegistry<CommandSourceStack> {

    @Override
    protected Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> initialiseParsers() {
        Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> parsers = super.initialiseParsers();

        parsers.putAll(Map.ofEntries(
          Map.entry(PlayerSenderRequirement.class, PlayerSenderRequirement.INSTANCE),
          Map.entry(ConsoleSenderRequirement.class, ConsoleSenderRequirement.INSTANCE)
        ));

        return parsers;
    }

}
