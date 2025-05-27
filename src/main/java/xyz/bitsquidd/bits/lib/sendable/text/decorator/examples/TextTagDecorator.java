package xyz.bitsquidd.bits.lib.sendable.text.decorator.examples;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.component.ComponentHelper;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.ITextDecorator;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.AbstractFormatter;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.StyleFormatter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TextTagDecorator implements ITextDecorator {
    protected final Set<AbstractFormatter> globalFormatters = new HashSet<>();
    protected final Set<AbstractFormatter> formatters = new HashSet<>();

    protected TextTagDecorator() {
        formatters.add(new StyleFormatter("b", TextDecoration.BOLD));
        formatters.add(new StyleFormatter("i", TextDecoration.ITALIC));
        formatters.add(new StyleFormatter("s", TextDecoration.STRIKETHROUGH));
        formatters.add(new StyleFormatter("u", TextDecoration.UNDERLINED));
        formatters.add(new StyleFormatter("o", TextDecoration.OBFUSCATED));
    }

    @Override
    public @NotNull Component format(@NotNull Component component, @Nullable CommandSender target) {
        String content = ComponentHelper.getContent(component, target instanceof Player player ? player.locale() : null);
        Component formatted = processContent(content);

        return Component.empty().append(formatted);
    }

    private @NotNull Component processContent(@NotNull String content) {
        if (content.isEmpty()) {
            return Component.empty();
        }

        List<TextToken> tokens = tokenize(content);
        return buildComponent(tokens);
    }

    private @NotNull List<TextToken> tokenize(@NotNull String content) {
        List<TextToken> tokens = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();
        Set<AbstractFormatter> activeFormatters = new HashSet<>();

        int i = 0;
        while (i < content.length()) {
            boolean tagProcessed = false;

            for (AbstractFormatter formatter : formatters) {
                if (i + formatter.getOpenTag().length() <= content.length() && content.startsWith(formatter.getOpenTag(), i)) {
                    if (!currentText.isEmpty()) {
                        tokens.add(new TextToken(currentText.toString(), new HashSet<>(activeFormatters)));
                        currentText = new StringBuilder();
                    }
                    activeFormatters.add(formatter);
                    i += formatter.getOpenTag().length();
                    tagProcessed = true;
                    break;
                }

                if (i + formatter.getCloseTag().length() <= content.length() && content.startsWith(formatter.getCloseTag(), i)) {
                    if (!currentText.isEmpty()) {
                        tokens.add(new TextToken(currentText.toString(), new HashSet<>(activeFormatters)));
                        currentText = new StringBuilder();
                    }
                    activeFormatters.remove(formatter);
                    i += formatter.getCloseTag().length();
                    tagProcessed = true;
                    break;
                }
            }

            if (!tagProcessed) {
                currentText.append(content.charAt(i));
                i++;
            }
        }

        if (!currentText.isEmpty()) {
            tokens.add(new TextToken(currentText.toString(), new HashSet<>(activeFormatters)));
        }

        return tokens;
    }

    private @NotNull Component buildComponent(@NotNull List<TextToken> tokens) {
        TextComponent.Builder builder = Component.text();

        for (TextToken token : tokens) {
            Component textPlaceholder = Component.text(token.text);

            Set<AbstractFormatter> activeFormatters = new HashSet<>(globalFormatters);
            activeFormatters.addAll(token.activeFormatters);

            for (AbstractFormatter formatter : activeFormatters) {
                textPlaceholder = formatter.format(textPlaceholder);
            }

            builder.append(textPlaceholder);
        }

        return builder.build();
    }

    private record TextToken(String text, Set<AbstractFormatter> activeFormatters) {}
}