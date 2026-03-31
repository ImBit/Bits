/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.config;

import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lifecycle.manager.ManagerContainer;
import xyz.bitsquidd.bits.log.Logger;

import java.util.Objects;

/**
 * The main configuration class for the Bits library.
 * <p>
 * This must be created for correct Bits functionality. It acts as the central hub
 * for accessing core services like the logger and command manager.
 *
 * @since 0.0.10
 */
public abstract class BitsConfig extends ManagerContainer {
    private static @Nullable BitsConfig instance;

    protected final boolean developmentMode = false;
    protected final Logger logger;

    /**
     * @throws IllegalStateException if a configuration instance already exists
     * @since 0.0.10
     */
    protected BitsConfig() {
        if (instance != null) throw new IllegalStateException("BitsConfig instance already exists!");
        instance = this;

        this.logger = createLogger();
    }

    /**
     * Retrieves the active configuration instance.
     *
     * @return the configuration instance
     *
     * @throws IllegalStateException if the configuration has not been created
     * @since 0.0.10
     */
    public static BitsConfig get() {
        return Objects.requireNonNull(instance, "BitsConfig instance has not been created yet.");
    }


    /**
     * Indicates whether the library is operating in development mode.
     * This is used for showing increased debugging.
     *
     * @return true if development mode is active, false otherwise
     *
     * @since 0.0.10
     */
    public boolean isDevelopment() {
        return developmentMode;
    }

    protected abstract Logger createLogger();


    public abstract void runLater(Runnable runnable, long delayMs);

    public abstract void runLaterAsync(Runnable runnable, long delayMs);

}
