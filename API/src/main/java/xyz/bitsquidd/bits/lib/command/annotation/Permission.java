package xyz.bitsquidd.bits.lib.command.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies an extra string permission to be required to execute the command.
 * <p>
 * Note: this is appended onto the end of the base permission string i.e. "bits.command.command_name.permission"
 * as there are few situations where you should need to share permissions between commands.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    @NotNull String[] value();
}