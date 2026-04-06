/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.impl.serializer.adventure;

import net.kyori.adventure.key.Key;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.StringNode;

import xyz.bitsquidd.bits.util.serializer.MultiSerializer;
import xyz.bitsquidd.bits.util.serializer.Serializer;

@Serializer
public final class KeySerializer extends MultiSerializer<Key> {
    private KeySerializer() {
        super(Key.class);
    }

    @Override
    protected JsonNode serialize(Key value) {
        return StringNode.valueOf(value.asString());
    }

    @Override
    protected Key deserialize(JsonNode node) {
        return Key.key(node.asString());
    }

}