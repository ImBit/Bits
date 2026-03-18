/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.wrapper;

import xyz.bitsquidd.bits.BitsConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * A callback is a stateful wrapper that can be used to execute code
 * after a certain event or action has occurred, as a lightweight
 * alternative to {@link java.util.concurrent.CompletableFuture}.
 */
public final class Callback {
    private boolean completed = false;
    private final List<Runnable> listeners = new ArrayList<>();

    private Callback() {}

    /**
     * Create a new incomplete Callback.
     */
    public static Callback create() {
        return new Callback();
    }

    /**
     * Create an already-completed Callback.
     * Any listeners registered on it will fire immediately.
     */
    public static Callback completed() {
        Callback callback = new Callback();
        callback.complete();
        return callback;
    }

    /**
     * Creates a Callback that will complete after a specified delay.
     *
     * @param delay The delay in milliseconds before the callback is completed.
     */
    public static Callback later(long delay) {
        Callback callback = new Callback();
        BitsConfig.get().runLater(callback::complete, delay);
        return callback;
    }


    /**
     * Mark this callback as complete and fire all registered listeners.
     */
    public void complete() {
        if (completed) return;
        completed = true;
        listeners.forEach(Runnable::run);
    }

    /**
     * Returns whether this callback has been completed.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Register a listener to run when this callback completes.
     * If already completed, runs immediately.
     * Returns {@code this} for chaining.
     */
    public Callback whenComplete(Runnable listener) {
        if (completed) {
            listener.run();
        } else {
            listeners.add(listener);
        }
        return this;
    }

}