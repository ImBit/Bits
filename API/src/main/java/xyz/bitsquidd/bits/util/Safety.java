/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.util;

import xyz.bitsquidd.bits.log.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * Utility class for safely executing tasks and handling exceptions.
 */
public final class Safety {
    private Safety() {}

    public static void safeExecute(final Runnable task) {
        safeExecute("unnamed", task);
    }

    public static void safeExecute(String name, final Runnable task) {
        try {
            task.run();
        } catch (final Exception e) {
            Logger.exception("Error during " + name + " task execution.", e);
        }
    }

    public static <T> T safeExecute(final Supplier<T> task, T defaultValue) {
        return safeExecute("unnamed", task, defaultValue);
    }

    public static <T> T safeExecute(String name, final Supplier<T> task, T defaultValue) {
        try {
            return task.get();
        } catch (final Exception e) {
            Logger.exception("Error during " + name + " task execution.", e);
            return defaultValue;
        }
    }


    public static void logExceptionNicely(String message, Throwable exception) {
        if (exception instanceof InvocationTargetException invocationTargetException) {
            logExceptionNicely(message, invocationTargetException.getCause());
        } else {
            Logger.exception(message, exception);
        }

    }


}
