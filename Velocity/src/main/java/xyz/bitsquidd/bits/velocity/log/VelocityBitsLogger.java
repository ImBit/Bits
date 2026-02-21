/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.velocity.log;

import xyz.bitsquidd.bits.log.LogType;
import xyz.bitsquidd.bits.log.Logger;
import xyz.bitsquidd.bits.log.pretty.PrettyLogLevel;


public class VelocityBitsLogger extends Logger {
    public final org.slf4j.Logger slf4j;

    public VelocityBitsLogger(org.slf4j.Logger slf4j, LogFlags flags) {
        super(flags);
        this.slf4j = slf4j;
    }

    @Override
    public org.slf4j.Logger slf4j() {
        return slf4j;
    }


    @Override
    public void debugInternal(final String msg) {
        System.out.println(LogType.DEBUG.format(msg));
    }

    @Override
    public void successInternal(final String msg) {
        System.out.println(LogType.SUCCESS.format(msg));
    }

    @Override
    public void infoInternal(final String msg) {
        System.out.println(LogType.INFO.format(msg));
    }

    @Override
    public void warningInternal(final String msg) {
        System.out.println(LogType.WARNING.format(msg));
    }

    @Override
    public void errorInternal(final String msg) {
        System.out.println(LogType.ERROR.format(msg));
        if (flags.logExtendedError()) {
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.out.println(LogType.ERROR.format("\tat " + element));
            }
        }
    }

    @Override
    public void exceptionInternal(final String msg, final Throwable throwable) {
        StackTraceElement origin = throwable.getStackTrace()[0];
        String header = String.format(
          "%s |-> Exception at %s.%s (%s:%d) - %s",
          msg,
          origin.getClassName(),
          origin.getMethodName(),
          origin.getFileName(),
          origin.getLineNumber(),
          throwable.getMessage()
        );
        System.out.println(LogType.ERROR.format(header));

        if (flags.logExtendedError()) {
            for (StackTraceElement element : throwable.getStackTrace()) {
                System.out.println(PrettyLogLevel.RED.formatMessage("\tat " + element));
            }
        }
    }

}