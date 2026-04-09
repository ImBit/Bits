/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.impl.serializer.joml;

import org.joml.Quaternionf;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import xyz.bitsquidd.bits.util.serializer.MultiSerializer;
import xyz.bitsquidd.bits.util.serializer.Serializer;


@Serializer
public final class QuaternionfSerializer extends MultiSerializer<Quaternionf> {
    private QuaternionfSerializer() {
        super(Quaternionf.class);
    }

    @Override
    protected JsonNode serialize(Quaternionf value) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("x", value.x);
        node.put("y", value.y);
        node.put("z", value.z);
        node.put("w", value.w);
        return node;
    }

    @Override
    protected Quaternionf deserialize(JsonNode node) {
        float x = node.get("x").asFloat();
        float y = node.get("y").asFloat();
        float z = node.get("z").asFloat();
        float w = node.get("w").asFloat();
        return new Quaternionf(x, y, z, w);
    }

}
