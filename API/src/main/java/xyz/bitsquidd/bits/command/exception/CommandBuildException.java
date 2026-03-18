/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command.exception;

/**
 * Thrown to indicate an error during the construction or registration of a command tree.
 * <p>
 * This exception usually indicates that a command is missing required annotations like
 * {@link xyz.bitsquidd.bits.command.annotation.Command}, or that its parameters cannot be reliably
 * mapped to recognized Brigadier argument types.
 * <p>
 * Example usage:
 * <pre>{@code
 * if (commandAnnotation == null) {
 *     throw new CommandBuildException("Class " + commandClass + " must be annotated with @Command");
 * }
 * }</pre>
 *
 * @since 0.0.10
 */
public class CommandBuildException extends RuntimeException {
    public CommandBuildException(String message) {
        super(message);
    }

    public CommandBuildException(String message, Throwable cause) {
        super(message, cause);
    }

}
