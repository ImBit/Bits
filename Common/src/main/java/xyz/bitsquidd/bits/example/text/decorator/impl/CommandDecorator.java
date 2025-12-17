package xyz.bitsquidd.bits.example.text.decorator.impl;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter.BasicColorFormatter;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.ColorLightenerFormatter;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.StyleDecorator;

import java.util.Locale;

public class CommandDecorator extends StyleDecorator {
    public final @NotNull CommandReturnType commandReturnType;

    public CommandDecorator(@NotNull CommandReturnType commandReturnType) {
        this.commandReturnType = commandReturnType;
        this.globalFormatters.add(new BasicColorFormatter(toMessageColor(commandReturnType)));
        this.formatters.put("b", new ColorLightenerFormatter(0.4f));
    }

    public static CommandDecorator of(@NotNull CommandReturnType commandReturnType) {
        return new CommandDecorator(commandReturnType);
    }

    @Override
    public @NotNull Component format(@NotNull Component component, @NotNull Locale locale) {
        return Component.empty()
              .append(Component.text(toMessageIcon(commandReturnType)))
              .append(super.format(component, locale));
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
