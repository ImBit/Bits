package xyz.bitsquidd.bits.lib.command.annotations;

import xyz.bitsquidd.bits.lib.command.requirements.CommandRequirement;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(RequireContainer.class)
public @interface Require {
    Class<? extends CommandRequirement> value();
    String[] args() default {};
    boolean sendFailMessages() default true;
}