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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TimerRunnables extends Runnables {
    private final Consumer<Integer> onTick;

    private final long delay;
    private final long period;

    private final Function<Integer, Boolean> stopCondition;
    private final Consumer<Integer> onStop;


    TimerRunnables(Builder builder) {
        super(builder);
        this.onTick = builder.onTick;

        this.period = builder.period;
        this.delay = builder.delay;

        this.stopCondition = builder.stopCondition;
        this.onStop = builder.onStop;
    }

    @Override
    public BukkitRunnable asRunnable() {
        BukkitRunnable runnable = new BukkitRunnable() {
            int tick = 0;

            @Override
            public void run() {
                if (stopCondition.apply(tick)) {
                    onStop.accept(tick);
                    this.cancel();
                    return;
                } else {
                    onTick.accept(tick);
                }

                tick++;
            }
        };

        if (isForced) PermanentRunnableStorage.add(runnable.getTaskId());
        return runnable;
    }

    @Override
    protected BukkitTask createTask(BukkitRunnable runnable) {
        if (isAsync) {
            return runnable.runTaskTimerAsynchronously(plugin, delay, period);
        } else {
            return runnable.runTaskTimer(plugin, delay, period);
        }
    }

    public static final class Builder extends AbstractRunnableBuilder<TimerRunnables, Builder> {
        private final Consumer<Integer> onTick;

        private final long period;
        private final long delay;

        private Function<Integer, Boolean> stopCondition = tick -> false;
        private Consumer<Integer> onStop = (tick) -> {};

        public Builder(Consumer<Integer> onTick, long period, long delay) {
            this.onTick = onTick;
            this.period = period;
            this.delay = delay;
        }

        public Builder stopCondition(Function<Integer, Boolean> stopCondition) {
            final Function<Integer, Boolean> previous = this.stopCondition;
            this.stopCondition = tick -> previous.apply(tick) || stopCondition.apply(tick);
            return this;
        }

        public Builder stopCondition(Supplier<Boolean> stopCondition) {
            return stopCondition(tick -> stopCondition.get());
        }

        public Builder onStop(Consumer<Integer> onStop) {
            this.onStop = onStop;
            return this;
        }

        public Builder onStop(Runnable onStop) {
            return onStop(tick -> onStop.run());
        }


        @Override
        public TimerRunnables buildInternal() {
            return new TimerRunnables(this);
        }

    }


}
