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

import xyz.bitsquidd.bits.log.BasicLogger;
import xyz.bitsquidd.bits.permission.Permission;

public class PaperBitsLogger extends BasicLogger {
    public PaperBitsLogger(JavaPlugin plugin, LogFlags flags) {
        super(plugin.getSLF4JLogger(), flags);
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
    protected void onLog(LogData logData) {
        super.onLog(logData);
        notifyStaff(logData);

    }

}