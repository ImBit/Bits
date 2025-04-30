package xyz.bitsquidd.bits.lib.sendable.text.decorator.examples;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.component.ComponentHelper;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.ITextDecorator;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.StyleFormatter;

import java.util.*;

public abstract class TextTagDecorator implements ITextDecorator {

    private final Set<StyleFormatter> formatters;

    protected TextTagDecorator() {
        this.formatters = new HashSet<>();

        formatters.add(new StyleFormatter("b", TextDecoration.BOLD));
        formatters.add(new StyleFormatter("i", TextDecoration.ITALIC));
        formatters.add(new StyleFormatter("s", TextDecoration.STRIKETHROUGH));
        formatters.add(new StyleFormatter("u", TextDecoration.UNDERLINED));
        formatters.add(new StyleFormatter("o", TextDecoration.OBFUSCATED));
    }

    @Override
    public @NotNull Component format(Component component, CommandSender target) {
        String content = ComponentHelper.getContent(component, target instanceof Player player ? player.locale() : null);
        Component formatted = processContent(content);

        return Component.empty()
                .append(Component.text(getPrefix() + " "))
                .append(formatted);
    }

    private Component processContent(String content) {
        if (content == null || content.isEmpty()) {
            return Component.empty();
        }

        List<TextToken> tokens = tokenize(content);
        return buildComponent(tokens);
    }

    private List<TextToken> tokenize(String content) {
        List<TextToken> tokens = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();
        Set<StyleFormatter> activeFormatters = new HashSet<>();

        int i = 0;
        while (i < content.length()) {
            boolean tagProcessed = false;

            for (StyleFormatter formatter : formatters) {
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

    private Component buildComponent(List<TextToken> tokens) {
        TextComponent.Builder builder = Component.text();

        for (TextToken token : tokens) {
            Style.Builder style = Style.style();

            style.color(TextColor.color(getColor()));

            for (StyleFormatter formatter : token.activeFormatters) {
                formatter.decorate(style);
            }

            builder.append(Component.text(token.text).style(style.build()));
        }

        return builder.build();
    }

    private static class TextToken {
        final String text;
        final Set<StyleFormatter> activeFormatters;

        TextToken(String text, Set<StyleFormatter> activeFormatters) {
            this.text = text;
            this.activeFormatters = activeFormatters;
        }
    }
}