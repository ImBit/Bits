package xyz.bitsquidd.bits.lib.command.requirement.impl;

import net.kyori.adventure.text.Component;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.info.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

public class ConsoleSenderRequirement extends BitsCommandRequirement {
    public static final ConsoleSenderRequirement INSTANCE = new ConsoleSenderRequirement();

    @Override
    public boolean test(@NotNull BitsCommandContext context) {
        return context.getSender() instanceof ConsoleCommandSender;
    }

    @Override
    public @Nullable Text getFailureMessage(@NotNull BitsCommandContext context) {
        return Text.of(Component.text("This command can only be executed by the console."));
    }
}