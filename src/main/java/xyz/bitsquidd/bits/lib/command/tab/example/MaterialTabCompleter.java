package xyz.bitsquidd.bits.lib.command.tab.example;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.command.tab.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialTabCompleter implements TabCompleter<Material> {
    public static final MaterialTabCompleter INSTANCE = new MaterialTabCompleter();

    @Override
    public @NotNull List<String> getPossibleValues(@NotNull CommandSender sender) {
        return Arrays.stream(Material.values())
                .filter(material -> !material.isLegacy())
                .map(material -> material.name().toLowerCase())
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable Material getValue(String arg) {
        return Material.getMaterial(arg); //TODO: do we want readable names?
    }

    @Override
    public @NotNull String getTypeString() {
        return "Material";
    }
}