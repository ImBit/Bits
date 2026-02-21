/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.log;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.bitsquidd.bits.log.LogType;
import xyz.bitsquidd.bits.log.Logger;
import xyz.bitsquidd.bits.log.pretty.PrettyLogLevel;
import xyz.bitsquidd.bits.permission.Permission;

public class PaperBitsLogger extends Logger {
    public final org.slf4j.Logger serverLogger;

    public PaperBitsLogger(JavaPlugin plugin, LogFlags flags) {
        super(flags);
        this.serverLogger = plugin.getSLF4JLogger();
    }

    private void notifyStaff(LogData logData) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (Permission.of("bits.log." + logData.type().id() + ".notify").hasPermission(player)) {
                notifyPlayer(logData, player);
            }
        });
    }

    protected void notifyPlayer(LogData logData, Player player) {
        // Here you can customise how the log is sent to a player.
    }


    @Override
    public org.slf4j.Logger slf4j() {
        return serverLogger;
    }


    @Override
    public void debugInternal(final String msg) {
        System.out.println(LogType.DEBUG.format(msg));
        notifyStaff(new LogData(msg, LogType.DEBUG));
    }

    @Override
    public void successInternal(final String msg) {
        System.out.println(LogType.SUCCESS.format(msg));
        notifyStaff(new LogData(msg, LogType.SUCCESS));
    }

    @Override
    public void infoInternal(final String msg) {
        System.out.println(LogType.INFO.format(msg));
        notifyStaff(new LogData(msg, LogType.INFO));
    }

    @Override
    public void warningInternal(final String msg) {
        System.out.println(LogType.WARNING.format(msg));
        notifyStaff(new LogData(msg, LogType.WARNING));
    }

    @Override
    public void errorInternal(final String msg) {
        System.out.println(LogType.ERROR.format(msg));
        if (flags.logExtendedError()) {
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.out.println(LogType.ERROR.format("\tat " + element));
            }
        }
        notifyStaff(new LogData(msg, LogType.ERROR));
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