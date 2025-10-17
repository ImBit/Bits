package xyz.bitsquidd.bits.lib.command.annotation;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies extra command requirements to be checked before executing the command.
 * Note: Classes are used to define a requirement, instances of said class should be registered.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {
    @NotNull Class<? extends BitsCommandRequirement>[] value();
}