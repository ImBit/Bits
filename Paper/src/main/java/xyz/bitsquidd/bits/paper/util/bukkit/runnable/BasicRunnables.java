/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.util.bukkit.runnable;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import xyz.bitsquidd.bits.paper.util.bukkit.wrapper.PermanentRunnableStorage;


public final class BasicRunnables extends Runnables {
    private final Runnable task;

    BasicRunnables(Builder builder) {
        super(builder);
        this.task = builder.task;
    }

    @Override
    public BukkitRunnable asRunnable() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        };

        if (isForced) PermanentRunnableStorage.add(runnable.getTaskId());
        return runnable;
    }

    @Override
    protected BukkitTask createTask(BukkitRunnable runnable) {
        if (isAsync) {
            return runnable.runTaskAsynchronously(plugin);
        } else {
            return runnable.runTask(plugin);
        }
    }

    public static final class Builder extends AbstractRunnableBuilder<BasicRunnables, Builder> {
        private final Runnable task;

        public Builder(Runnable task) {
            this.task = task;
        }

        @Override
        public BasicRunnables buildInternal() {
            return new BasicRunnables(this);
        }

    }


}
