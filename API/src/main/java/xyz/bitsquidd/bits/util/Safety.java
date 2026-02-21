/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.util;

import xyz.bitsquidd.bits.log.Logger;

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

}
