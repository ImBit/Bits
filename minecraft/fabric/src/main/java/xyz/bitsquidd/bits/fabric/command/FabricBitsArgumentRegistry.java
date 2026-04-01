/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.fabric.command;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;

import xyz.bitsquidd.bits.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.fabric.command.provider.BitsCommandProvider;
import xyz.bitsquidd.bits.wrapper.collection.AddableSet;

import static xyz.bitsquidd.bits.fabric.command.FabricBitsCommandManager.COMMAND_INSTANCE_ENTRYPOINT;

public class FabricBitsArgumentRegistry extends BitsArgumentRegistry<CommandSourceStack> {

    /**
     * Fabric mods must use the {@code COMMAND_INSTANCE_ENTRYPOINT} entrypoint to provide command parsers.
     */
    @Override
    protected AddableSet<AbstractArgumentParser<?>> initialiseParsers() {
        return super.initialiseParsers().addAll(FabricLoader.getInstance()
          .getEntrypoints(COMMAND_INSTANCE_ENTRYPOINT, BitsCommandProvider.class)
          .stream()
          .flatMap(provider -> provider.getArguments().build().stream())
          .toList()
        );
    }

}
