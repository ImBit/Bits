/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.log;

import org.jetbrains.annotations.Nullable;

/**
 * A logger that can be used to log messages to the console. Must be initialised before use.
 */
public abstract class Logger {
    private static @Nullable Logger instance;

    protected final LogFlags flags;

    public record LogData(
      String msg,
      LogType type
    ) {}

    public record LogFlags(
      boolean logSuccess,
      boolean logDebug,
      boolean logInfo,
      boolean logWarn,
      boolean logError,
      boolean logException,

      boolean logExtendedError
    ) {
        public static LogFlags defaultFlags() {
            return new LogFlags(true, false, true, true, true, true, false);
        }

    }


    public Logger(LogFlags flags) {
        this.flags = flags;
        if (instance != null) throw new IllegalStateException("Logger is already initialized");
        instance = this;
    }

    public static Logger get() {
        if (instance == null) throw new IllegalStateException("Logger is not initialized");
        return instance;
    }


    public abstract org.slf4j.Logger slf4j();


    protected abstract void debugInternal(final String msg);

    public static void debug(final String msg) {
        if (!get().flags.logDebug()) return;
        get().debugInternal(msg);
    }

    protected abstract void successInternal(final String msg);

    public static void success(final String msg) {
        if (!get().flags.logSuccess()) return;
        get().successInternal(msg);
    }

    protected abstract void infoInternal(final String msg);

    public static void info(final String msg) {
        if (!get().flags.logInfo()) return;
        get().infoInternal(msg);
    }

    protected abstract void warningInternal(final String msg);

    public static void warn(final String msg) {
        if (!get().flags.logWarn()) return;
        get().warningInternal(msg);
    }

    protected abstract void errorInternal(final String msg);

    public static void error(final String msg) {
        if (!get().flags.logError()) return;
        get().errorInternal(msg);
    }

    protected abstract void exceptionInternal(final String msg, final Throwable throwable);

    public static void exception(final String msg, final Throwable throwable) {
        if (!get().flags.logException()) return;
        get().exceptionInternal(msg, throwable);
    }

}
