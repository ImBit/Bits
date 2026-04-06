/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.impl.serializer.bukkit;

import org.bukkit.util.Vector;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;

import xyz.bitsquidd.bits.util.serializer.MultiSerializer;
import xyz.bitsquidd.bits.util.serializer.Serializer;


@Serializer
public final class VectorSerializer extends MultiSerializer<Vector> {
    private VectorSerializer() {
        super(Vector.class);
    }

    @Override
    protected JsonNode serialize(Vector value) {
        ObjectNode node = new ObjectNode(null);
        node.put("x", value.getX());
        node.put("y", value.getY());
        node.put("z", value.getZ());
        return node;
    }

    @Override
    protected Vector deserialize(JsonNode node) {
        float x = node.get("x").asFloat();
        float y = node.get("y").asFloat();
        float z = node.get("z").asFloat();
        return new Vector(x, y, z);
    }

}
