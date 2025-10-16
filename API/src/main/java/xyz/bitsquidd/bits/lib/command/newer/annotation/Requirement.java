package xyz.bitsquidd.bits.lib.command.newer.annotation;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.requirement.BitsCommandRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {
    @NotNull Class<? extends BitsCommandRequirement>[] value();
}