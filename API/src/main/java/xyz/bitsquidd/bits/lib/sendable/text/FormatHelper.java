package xyz.bitsquidd.bits.lib.sendable.text;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public class FormatHelper {
    public static String formatLocation(Location location, boolean includeWorld) {
        StringBuilder sb = new StringBuilder();

        if (includeWorld) sb.append(location.getWorld().getName()).append(": ");

        sb.append(String.format("%.1f", location.getX())).append(", ");
        sb.append(String.format("%.1f", location.getY())).append(", ");
        sb.append(String.format("%.1f", location.getZ()));
        return sb.toString();
    }

    public static String formatPlayers(Collection<Player> players) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Player player : players) {
            if (i > 0) sb.append(", ");
            sb.append(player.getName());
            i++;
        }
        return sb.toString();
    }
}
