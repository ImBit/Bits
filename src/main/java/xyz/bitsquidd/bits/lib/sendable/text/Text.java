package xyz.bitsquidd.bits.lib.sendable.text;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.sendable.Sendable;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.ITextDecorator;

public class Text implements Sendable {
    protected final @NotNull Component component;
    protected final @Nullable ITextDecorator decorator;

    public Text(@NotNull Component component, @Nullable ITextDecorator decorator) {
        this.component = component;
        this.decorator = decorator;
    }
    public Text(@NotNull Component component) {
        this(component, null);
    }
    public Text(@NotNull String text, @Nullable ITextDecorator formatter) {
        this(Component.text(text), formatter);
    }
    public Text(@NotNull String text) {
        this(Component.text(text), null);
    }

    public static Text of(@NotNull Component component, @Nullable ITextDecorator formatter) {
        return new Text(component, formatter);
    }
    public static Text of(@NotNull Component component) {
        return new Text(component);
    }
    public static Text of(@NotNull String text, @Nullable ITextDecorator formatter) {
        return new Text(text, formatter);
    }
    public static Text of(@NotNull String text) {
        return new Text(text);
    }

    @Override
    public <T extends CommandSender> void send(@NotNull T target) {
        target.sendMessage(getComponent(target));
    }

    public <T extends CommandSender> Component getComponent(@Nullable T target) {
        return decorator != null ? decorator.format(component, target) : component;
    }
}
