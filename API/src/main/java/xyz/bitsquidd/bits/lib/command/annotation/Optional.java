package xyz.bitsquidd.bits.lib.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a command method parameter as optional.
 * Optional parameters are effectively {@link org.jetbrains.annotations.Nullable}, and should not be assumed to be passed into the method.
 * <p>
 * <b>This should only be used to annotate the last parameters; otherwise, this may lead to unexpected behavior.</b>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Optional {
}