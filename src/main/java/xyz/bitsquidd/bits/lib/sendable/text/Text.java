package xyz.bitsquidd.bits.lib.sendable.text;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.sendable.Sendable;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.ITextDecorator;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.examples.BlankDecorator;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.examples.TranslationDecorator;

import java.util.ArrayList;
import java.util.List;

public class Text implements Sendable {
    protected final @NotNull Component component;
    protected final @NotNull List<ITextDecorator> decorators = new ArrayList<>();

    private static final List<ITextDecorator> PRE_DEFAULT_DECORATORS = List.of(
            // These will always be applied first.
            new TranslationDecorator()
    );
    private static final List<ITextDecorator> POST_DEFAULT_DECORATORS = List.of(
            // These will always be applied last.
            new BlankDecorator()
    );

    public Text(@NotNull Component component, @NotNull List<ITextDecorator> decorators) {
        this.component = component;
        this.decorators.addAll(decorators);
    }

    public Text(@NotNull Component component, @NotNull ITextDecorator decorator) {
        this(component, List.of(decorator));
    }

    public Text(@NotNull Component component) {
        this(component, List.of());
    }

    @Deprecated(forRemoval = true)
    public Text(@NotNull String text, @NotNull ITextDecorator formatter) {
        this(Component.text(text), formatter);
    }

    @Deprecated(forRemoval = true)
    public Text(@NotNull String text) {
        this(Component.text(text), List.of());
    }


    public static Text of(@NotNull Component component, @NotNull List<ITextDecorator> decorators) {
        return new Text(component, decorators);
    }

    public static Text of(@NotNull Component component, @NotNull ITextDecorator decorator) {
        return new Text(component, decorator);
    }

    public static Text of(@NotNull Component component) {
        return new Text(component);
    }

    @Deprecated(forRemoval = true)
    public static Text of(@NotNull String text, @Nullable ITextDecorator formatter) {
        return new Text(text, formatter);
    }
    @Deprecated(forRemoval = true)
    public static Text of(@NotNull String text) {
        return new Text(text);
    }



    @Override
    public <T extends CommandSender> void send(@NotNull T target) {
        target.sendMessage(getComponent(target));
    }

    public <T extends CommandSender> Component getComponent(@Nullable T target) {
        Component returnComponent = component;

        List<ITextDecorator> componentDecorators = new ArrayList<>(PRE_DEFAULT_DECORATORS);
        componentDecorators.addAll(decorators);
        componentDecorators.addAll(POST_DEFAULT_DECORATORS);

        for (ITextDecorator decorator : componentDecorators) {
            returnComponent = decorator.format(returnComponent, target);
        }

        return returnComponent;
    }
}
