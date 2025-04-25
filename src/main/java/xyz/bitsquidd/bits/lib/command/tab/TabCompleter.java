package xyz.bitsquidd.bits.lib.command.tab;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public interface TabCompleter<T> {
    @NotNull List<String> getPossibleValues(@NotNull CommandSender sender);

    @Nullable T getValue(String arg);
    @NotNull String getTypeString();

    default @NotNull List<String> complete(@NotNull CommandSender sender, @NotNull String currentArg) {
        return filterSuggestions(getPossibleValues(sender), currentArg);
    }

    default @NotNull List<String> filterSuggestions(@NotNull List<String> suggestions, @NotNull String currentArg) {
        return suggestions.stream()
                .filter(suggestion -> suggestion.toLowerCase().startsWith(currentArg.toLowerCase()))
                .collect(Collectors.toList());
    }
}