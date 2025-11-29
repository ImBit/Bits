package xyz.bitsquidd.bits.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.audience.Audience;
import org.slf4j.Logger;

import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.lib.permission.Permission;

import java.util.Locale;

public class VelocityBitsConfig extends BitsConfig {
    private final ProxyServer server;

    public VelocityBitsConfig(ProxyServer server, Logger logger) {
        super(logger);
        this.server = server;
    }


    public ProxyServer getServer() {
        checkInitialized();
        return server;
    }

    @Override
    public boolean hasPermission(Audience audience, Permission permission) {
        checkInitialized();
        if (audience instanceof CommandSource commandSource) {
            return commandSource.hasPermission(permission.toString());
        } else {
            return false;
        }
    }

    @Override
    public Locale getLocale(Audience audience) {
        checkInitialized();
        if (audience instanceof Player player) {
            Locale locale = player.getEffectiveLocale();
            if (locale != null) player.getEffectiveLocale();
        }
        return Locale.getDefault();
    }

}
