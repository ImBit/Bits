package xyz.bitsquidd.bits.lib.command.newer.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class or method as a command.
 * When applied to a class, creates a command group.
 * When applied to a method, creates an executable command.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    @NotNull String value() default "";

    @NotNull String[] aliases() default {};

    @NotNull String description() default "";
}