package xyz.bitsquidd.bits.lib.sendable.text.decorator.impl;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter.BasicColorFormatter;

public class CommandReturnDecorator extends StyleDecorator {
    private final @NotNull CommandReturnType commandReturnType;

    private CommandReturnDecorator(@NotNull CommandReturnType commandReturnType) {
        super();
        this.commandReturnType = commandReturnType;

        this.globalFormatters.add(new BasicColorFormatter(commandReturnType.color));
    }

    public static @NotNull CommandReturnDecorator of(@NotNull CommandReturnType commandReturnType) {
        return new CommandReturnDecorator(commandReturnType);
    }

    @Override
    public @NotNull Component format(@NotNull Component component, @Nullable Audience target) {
        return Component.text(commandReturnType.icon + " ").append(super.format(component, target));
    }
}
