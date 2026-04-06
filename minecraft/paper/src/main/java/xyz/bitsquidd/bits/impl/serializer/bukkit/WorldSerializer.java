/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.impl.serializer.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.World;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.StringNode;

import xyz.bitsquidd.bits.util.serializer.MultiSerializer;
import xyz.bitsquidd.bits.util.serializer.Serializer;


@Serializer
public final class WorldSerializer extends MultiSerializer<World> {
    private WorldSerializer() {
        super(World.class);
    }

    @Override
    protected JsonNode serialize(World value) {
        return StringNode.valueOf(value.getName());
    }

    @Override
    protected World deserialize(JsonNode node) {
        return Bukkit.getWorld(node.asString());
    }

}
