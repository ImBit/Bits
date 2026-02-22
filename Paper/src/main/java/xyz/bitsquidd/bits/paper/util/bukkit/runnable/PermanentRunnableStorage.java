/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.util.bukkit.runnable;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A special storage for runnables that should never be cancelled.
 * By default all runnables should be cancelled on {@link xyz.bitsquidd.bits.paper.lifecycle.manager.PaperManagerContainer#cleanup() PaperManagerContainer#cleanup()}.
 */
public final class PermanentRunnableStorage {
    private PermanentRunnableStorage() {}

    private static final Set<Integer> runnables = ConcurrentHashMap.newKeySet();

    public static void add(int taskId) {
        runnables.add(taskId);
    }

    public static void remove(int taskId) {
        runnables.remove(taskId);
    }

    public static boolean contains(int taskId) {
        return runnables.contains(taskId);
    }

}
