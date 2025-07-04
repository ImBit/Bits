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

/**
 * A sendable message, supporting complex formatting.
 *
 * <p>This class wraps an Adventure {@link net.kyori.adventure.text.Component} and allows for the application
 * of multiple {@link xyz.bitsquidd.bits.lib.sendable.text.decorator.ITextDecorator} instances to format the text before sending.
 * Decorators are applied in the order:
 * <ol>
 *   <li>Pre-default decorators (always applied first)</li>
 *   <li>Custom decorators (specified during construction)</li>
 *   <li>Post-default decorators (always applied last)</li>
 * </ol>
 *
 * <p>Default decorators include:
 * <ul>
 *   <li>{@link xyz.bitsquidd.bits.lib.sendable.text.decorator.examples.TranslationDecorator} - Applied first to render {@link net.kyori.adventure.text.TranslatableComponent}s</li>
 *   <li>{@link xyz.bitsquidd.bits.lib.sendable.text.decorator.examples.BlankDecorator} - Applied last as a final processing to remove regular tags</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * // Simple text without decorators
 * Text.of("Hello World").send(player);
 *
 * // Text with custom decorator
 * Text.of(Component.text("Formatted text"), new ColorDecorator()).send(player);
 *
 * // Text with multiple decorators
 * Text.of("Complex text", List.of(decorator1, decorator2)).send(player);
 *
 * // Text with formatting tags
 * Text.of("This <b>text is bold<b> and this is <i>italic<i>, <b,i>this is both</b,/i>", new FancyTagDecorator()).send(player);
 * }</pre>
 */
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

    @Deprecated(forRemoval = true, since = "0.0.4")
    public static Text of(@NotNull Component component, @NotNull List<ITextDecorator> decorators) {
        return new Text(component, decorators);
    }

    @Deprecated(forRemoval = true, since = "0.0.4")
    public static Text of(@NotNull Component component, @NotNull ITextDecorator decorator) {
        return new Text(component, decorator);
    }

    @Deprecated(forRemoval = true, since = "0.0.4")
    public static Text of(@NotNull String text, @NotNull ITextDecorator decorator) {
        return new Text(Component.text(text), decorator);
    }

    @Deprecated(forRemoval = true, since = "0.0.4")
    public static Text of(@NotNull String text) {
        return new Text(Component.text(text));
    }

    public static Text of(@NotNull Component component) {
        return new Text(component);
    }

    public Text decorate(@NotNull List<ITextDecorator> decorators) {
        return new Text(component, decorators);
    }

    public Text decorate(@NotNull ITextDecorator... decorators) {
        return new Text(component, new ArrayList<>(List.of(decorators)));
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
