package xyz.bitsquidd.bits.lib.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    String name();
    String description() default "";
    String permission() default "";
    String permissionMessage() default "You don't have permission to use this command.";
    String[] aliases() default {};
    String usage() default "";
    boolean playerOnly() default false;
}