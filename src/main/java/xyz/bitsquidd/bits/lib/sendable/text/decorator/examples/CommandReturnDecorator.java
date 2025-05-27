package xyz.bitsquidd.bits.lib.sendable.text.decorator.examples;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.ColorFormatter;

public class CommandReturnDecorator extends TextTagDecorator {
    private final @NotNull CommandReturnType commandReturnType;

    public CommandReturnDecorator(@NotNull CommandReturnType commandReturnType) {
        super();
        this.commandReturnType = commandReturnType;

        this.globalFormatters.add(new ColorFormatter("", commandReturnType.color));
    }

    @Override
    public @NotNull Component format(@NotNull Component component, @Nullable CommandSender target) {
        return Component.text(commandReturnType.icon+" ").append(super.format(component, target));
    }
}
