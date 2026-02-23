/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.velocity.util.velocity.runnable;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler;
import com.velocitypowered.api.scheduler.TaskStatus;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.velocity.VelocityBitsConfig;

import java.util.function.Consumer;

public final class Tasks {
    private static final Object plugin = VelocityBitsConfig.get().getPlugin();
    private static final Scheduler scheduler = VelocityBitsConfig.get().getServer().getScheduler();
    private static final EventManager eventManager = VelocityBitsConfig.get().getServer().getEventManager();

    public static Scheduler.TaskBuilder builder(Runnable runnable) {
        return scheduler.buildTask(VelocityBitsConfig.get().getPlugin(), runnable);
    }

    public static Scheduler.TaskBuilder builder(Consumer<ScheduledTask> consumer) {
        return scheduler.buildTask(VelocityBitsConfig.get().getPlugin(), consumer);
    }


    public static @Nullable ScheduledTask cleanup(final @Nullable ScheduledTask task) {
        if (task != null && task.status() == TaskStatus.SCHEDULED) task.cancel();
        return null;
    }

    public static void cleanupAll() {
        scheduler.tasksByPlugin(plugin).forEach(Tasks::cleanup);
    }


    public static void callEvent(Object object) {
        eventManager.fire(object);
    }

}
