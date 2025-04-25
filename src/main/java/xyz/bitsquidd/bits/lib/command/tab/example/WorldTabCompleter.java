package xyz.bitsquidd.bits.lib.command.tab.example;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.command.tab.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;

public class WorldTabCompleter implements TabCompleter<World> {
    public static final WorldTabCompleter INSTANCE = new WorldTabCompleter();

    @Override
    public @NotNull List<String> getPossibleValues(@NotNull CommandSender sender) {
        return Bukkit.getWorlds().stream()
                .map(World::getName)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable World getValue(String arg) {
        return Bukkit.getWorld(arg);
    }

    @Override
    public @NotNull String getTypeString() {
        return "World";
    }
}