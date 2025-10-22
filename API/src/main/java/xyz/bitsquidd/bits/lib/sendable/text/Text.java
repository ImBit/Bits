package xyz.bitsquidd.bits.lib.sendable.text;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.sendable.Sendable;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.ITextDecorator;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.BlankDecorator;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.TranslationDecorator;

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
 *   <li>{@link xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.TranslationDecorator} - Applied first to render {@link net.kyori.adventure.text.TranslatableComponent}s</li>
 *   <li>{@link xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.BlankDecorator} - Applied last as a final processing to remove regular tags</li>
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
@NullMarked
public final class Text implements Sendable {
    private final Component component;
    private final List<ITextDecorator> decorators = new ArrayList<>();

    private static final List<ITextDecorator> PRE_DEFAULT_DECORATORS = List.of(
          // These will always be applied first.
          new TranslationDecorator()
    );
    private static final List<ITextDecorator> POST_DEFAULT_DECORATORS = List.of(
          // These will always be applied last.
          new BlankDecorator()
    );

    private Text(Component component, List<ITextDecorator> decorators) {
        this.component = component;
        this.decorators.addAll(decorators);
    }

    private Text(Component component, ITextDecorator decorator) {
        this(component, List.of(decorator));
    }

    private Text(Component component) {
        this(component, List.of());
    }


    public static Text of(Component component) {
        return new Text(component);
    }

    public static Text of(String plainText) {
        return new Text(Component.text(plainText));
    }


    public Text decorate(List<ITextDecorator> decorators) {
        return new Text(component, decorators);
    }

    public Text decorate(ITextDecorator... decorators) {
        return new Text(component, new ArrayList<>(List.of(decorators)));
    }


    @Override
    public <T extends Audience> void send(T target) {
        target.sendMessage(getComponent(target));
    }

    public <T extends Audience> Component getComponent(@Nullable T target) {
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
