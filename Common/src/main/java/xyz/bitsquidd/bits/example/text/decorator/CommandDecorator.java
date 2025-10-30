package xyz.bitsquidd.bits.example.text.decorator;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter.BasicColorFormatter;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.StyleDecorator;

public class CommandDecorator extends StyleDecorator {
    public final @NotNull CommandReturnType commandReturnType;

    public CommandDecorator(@NotNull CommandReturnType commandReturnType) {
        this.commandReturnType = commandReturnType;
        this.globalFormatters.add(new BasicColorFormatter(toMessageColor(commandReturnType)));
    }

    public static CommandDecorator of(@NotNull CommandReturnType commandReturnType) {
        return new CommandDecorator(commandReturnType);
    }

    @Override
    public @NotNull Component format(@NotNull Component component, @Nullable Audience target) {
        return Component.empty()
              .append(Component.text(toMessageIcon(commandReturnType)))
              .append(super.format(component, target));
    }

    private static int toMessageColor(CommandReturnType commandReturnType) {
        return switch (commandReturnType) {
            case SUCCESS -> 0x77FF00;
            case INFO -> 0xBBBBFF;
            case ERROR -> 0xFF2244;
            default -> 0x6622DD;
        };
    }

    private static String toMessageIcon(CommandReturnType commandReturnType) {
        return switch (commandReturnType) {
            case SUCCESS -> "✔";
            case INFO -> "ℹ";
            case ERROR -> "❌";
            default -> ">";
        };
    }

}
