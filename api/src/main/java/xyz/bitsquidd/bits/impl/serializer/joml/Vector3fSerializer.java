/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.impl.serializer.joml;

import org.joml.Vector3f;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;

import xyz.bitsquidd.bits.util.serializer.MultiSerializer;
import xyz.bitsquidd.bits.util.serializer.Serializer;

@Serializer
public final class Vector3fSerializer extends MultiSerializer<Vector3f> {
    private Vector3fSerializer() {
        super(Vector3f.class);
    }

    @Override
    protected JsonNode serialize(Vector3f value) {
        ObjectNode node = new ObjectNode(null);
        node.put("x", value.x);
        node.put("y", value.y);
        node.put("z", value.z);
        return node;
    }

    @Override
    protected Vector3f deserialize(JsonNode node) {
        float x = node.get("x").asFloat();
        float y = node.get("y").asFloat();
        float z = node.get("z").asFloat();
        return new Vector3f(x, y, z);
    }

}
