package xyz.bitsquidd.bits.paper;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.lib.permission.Permission;

import java.util.Locale;

public class PaperBitsConfig extends BitsConfig {
    private final JavaPlugin plugin;

    public PaperBitsConfig(JavaPlugin plugin) {
        super(plugin.getSLF4JLogger());
        this.plugin = plugin;
    }


    public JavaPlugin getPlugin() {
        checkInitialized();
        return plugin;
    }

    @Override
    public boolean hasPermission(Audience audience, Permission permission) {
        checkInitialized();
        if (audience instanceof CommandSender commandSender) {
            return commandSender.hasPermission(permission.toString());
        } else {
            return false;
        }
    }

    @Override
    public Locale getLocale(Audience audience) {
        checkInitialized();
        if (audience instanceof Player player) {
            return player.locale();
        }
        return Locale.getDefault();
    }

    @Override
    public Audience getAll() {
        return Audience.audience(Bukkit.getOnlinePlayers());
    }

}
