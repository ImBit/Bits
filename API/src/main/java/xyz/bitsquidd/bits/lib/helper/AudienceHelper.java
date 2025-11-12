package xyz.bitsquidd.bits.lib.helper;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class AudienceHelper {

    public static @NotNull List<Player> getPlayers(@NotNull Audience audience) {
        List<Player> players = new ArrayList<>();

        if (audience instanceof Player player) {
            players.add(player);
            return players;
        } else {
            audience.forEachAudience(containedAudience -> {
                if (containedAudience instanceof Player player) {
                    players.add(player);
                }
            });
        }

        return players;
    }
}
