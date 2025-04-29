package xyz.bitsquidd.bits.lib.command.annotations;

import xyz.bitsquidd.bits.lib.command.requirement.CommandRequirement;

import java.lang.annotation.*;

/**
 * Specifies one or more requirements that must be met for a command path to execute.
 * Can be applied to both classes and methods.
 * When applied to a class, all paths in that class inherit these requirements.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(RequireContainer.class)
public @interface Require {
    /**
     * The class that implements CommandRequirement.
     */
    Class<? extends CommandRequirement> value();
    
    /**
     * Arguments to pass to the requirement constructor.
     */
    String[] args() default {};
    
    /**
     * Whether to send failure messages automatically when requirements fail.
     */
    boolean sendFailMessages() default true;
}