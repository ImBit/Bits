/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.util.serializer;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.*;

public abstract class MultiSerializer<T> {
    private final Class<T> clazz;

    protected MultiSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    public final Class<T> getTargetClass() {
        return clazz;
    }


    protected abstract JsonNode serialize(T value) throws JacksonException;

    public final ValueSerializer<T> jacksonSerializer() {
        return new ValueSerializer<T>() {
            @Override
            public void serialize(T value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
                JsonNode node = MultiSerializer.this.serialize(value);
                gen.writeTree(node);
            }
        };
    }


    protected abstract T deserialize(JsonNode node) throws JacksonException;

    public final ValueDeserializer<T> jacksonDeserializer() {
        return new ValueDeserializer<T>() {
            @Override
            public T deserialize(JsonParser parser, DeserializationContext ctx) throws JacksonException {
                JsonNode node = ctx.readTree(parser);
                return MultiSerializer.this.deserialize(node);
            }
        };
    }

}