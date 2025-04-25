package xyz.bitsquidd.bits.lib.command.tab.example;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.command.tab.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerTabCompleter implements TabCompleter<Player> {
    public static final PlayerTabCompleter INSTANCE = new PlayerTabCompleter();

    @Override
    public @NotNull List<String> getPossibleValues(@NotNull CommandSender sender) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable Player getValue(String arg) {
        return Bukkit.getPlayer(arg);
    }

    @Override
    public @NotNull String getTypeString() {
        return "Player";
    }
}