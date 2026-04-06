/*
 * This file is part of the BiomeBattle project.
 *
 * Copyright (c) 2023-2026 BitSquidd - All Rights Reserved
 *
 * Unauthorized copying, distribution, or disclosure of this file, via any medium, is strictly prohibited.
 * Proprietary and confidential - for internal use only.
 */

package xyz.bitsquidd.bits.impl.serializer.time;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.LongNode;

import xyz.bitsquidd.bits.util.serializer.MultiSerializer;
import xyz.bitsquidd.bits.util.serializer.Serializer;

import java.time.Duration;


@Serializer
public final class DurationSerializer extends MultiSerializer<Duration> {
    private DurationSerializer() {
        super(Duration.class);
    }

    @Override
    protected JsonNode serialize(Duration value) {
        return LongNode.valueOf(value.toMillis());
    }

    @Override
    protected Duration deserialize(JsonNode node) {
        return Duration.ofMillis(node.asLong());
    }

}