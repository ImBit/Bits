/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.impl.serializer;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.StringNode;

import xyz.bitsquidd.bits.util.serializer.MultiSerializer;
import xyz.bitsquidd.bits.util.serializer.Serializer;

import java.util.UUID;


@Serializer
public final class UUIDSerializer extends MultiSerializer<UUID> {
    private UUIDSerializer() {
        super(UUID.class);
    }

    @Override
    protected JsonNode serialize(UUID value) {
        return StringNode.valueOf(value.toString());
    }

    @Override
    protected UUID deserialize(JsonNode node) {
        return UUID.fromString(node.asString());
    }

}