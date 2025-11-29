package xyz.bitsquidd.bits.lib.command.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Allows command parameters to have custom argument names.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
    String value();
}
