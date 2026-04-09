/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.util.serializer.jackson;

import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.databind.introspect.AnnotatedMember;
import tools.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * Custom Jackson annotation introspector that treats fields annotated with @Nullable as optional (not required).
 * It forces the serializer to treat everything as required unless @Nullable.
 */
public class NullableAwareIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public Boolean hasRequiredMarker(MapperConfig<?> config, AnnotatedMember m) {
        if (m.hasAnnotation(org.jetbrains.annotations.Nullable.class) || m.getAnnotation(org.jspecify.annotations.Nullable.class) != null) {
            return false;
        }

        return super.hasRequiredMarker(config, m) != null
               ? super.hasRequiredMarker(config, m)
               : true;
    }

}