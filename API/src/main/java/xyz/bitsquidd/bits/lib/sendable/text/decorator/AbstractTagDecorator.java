package xyz.bitsquidd.bits.lib.sendable.text.decorator;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter.AbstractFormatter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractTagDecorator implements ITextDecorator {
    protected final LinkedHashSet<AbstractFormatter> globalFormatters = new LinkedHashSet<>();
    protected final LinkedHashMap<String, AbstractFormatter> formatters = new LinkedHashMap<>();
    private static final Pattern TAG_PATTERN = Pattern.compile("<([^>]+)>");

    @Override
    public @NotNull Component format(@NotNull Component component, @Nullable Audience target) {
        List<FormattedTextToken> extractedTokens = extractComponentTokens(component, target);
        List<TextToken> processedTokens = processTokensWithTags(extractedTokens);
        return buildComponent(processedTokens);
    }

    private @NotNull List<FormattedTextToken> extractComponentTokens(@NotNull Component component, @Nullable Audience target) {
        List<FormattedTextToken> tokens = new ArrayList<>();
        extractTokensRecursively(component, Style.empty(), tokens, target);
        return tokens;
    }

    private void extractTokensRecursively(@NotNull Component component, @NotNull Style inheritedStyle, @NotNull List<FormattedTextToken> tokens, @Nullable Audience target) {
        Style mergedStyle = inheritedStyle.merge(component.style());

        if (component instanceof TextComponent textComponent) {
            String content = textComponent.content();
            if (!content.isEmpty()) {
                tokens.add(new FormattedTextToken(content, mergedStyle));
            }
        }

        for (Component child : component.children()) {
            extractTokensRecursively(child, mergedStyle, tokens, target);
        }
    }

    private @NotNull List<TextToken> processTokensWithTags(@NotNull List<FormattedTextToken> extractedTokens) {
        List<TextToken> result = new ArrayList<>();

        for (FormattedTextToken token : extractedTokens) {
            List<AbstractFormatter> baseFormatters = AdventureFormatterFactory.fromStyle(token.style);

            List<TextToken> tagTokens = tokenizeWithTags(token.text, baseFormatters);
            result.addAll(tagTokens);
        }

        return result;
    }

    private @NotNull List<TextToken> tokenizeWithTags(@NotNull String content, @NotNull List<AbstractFormatter> baseFormatters) {
        List<TextToken> tokens = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();
        HashMap<String, String> activeTags = new LinkedHashMap<>();
        Matcher matcher = TAG_PATTERN.matcher(content);
        int lastEnd = 0;

        while (matcher.find()) {
            String beforeTag = content.substring(lastEnd, matcher.start());
            currentText.append(beforeTag);
            String tagContent = matcher.group(1);

            if (!currentText.isEmpty()) {
                List<AbstractFormatter> tokenFormatters = new ArrayList<>(baseFormatters);
                tokenFormatters.addAll(getTagFormatters(activeTags));
                tokens.add(new TextToken(currentText.toString(), tokenFormatters));
                currentText = new StringBuilder();
            }

            processTagOperations(tagContent, activeTags);
            lastEnd = matcher.end();
        }

        if (lastEnd < content.length()) currentText.append(content.substring(lastEnd));
        if (!currentText.isEmpty()) {
            List<AbstractFormatter> tokenFormatters = new ArrayList<>(baseFormatters);
            tokenFormatters.addAll(getTagFormatters(activeTags));
            tokens.add(new TextToken(currentText.toString(), tokenFormatters));
        }

        return tokens;
    }

    private void processTagOperations(@NotNull String tagContent, @NotNull HashMap<String, String> activeTags) {
        boolean hasLeadingSlash = tagContent.startsWith("/");
        if (hasLeadingSlash) tagContent = tagContent.substring(1);

        String[] tagParts = tagContent.split(",");
        for (String tagPart : tagParts) {
            tagPart = tagPart.trim();
            if (tagPart.isEmpty()) continue;

            boolean isClosing = tagPart.startsWith("/");
            String tagName = isClosing ? tagPart.substring(1).trim() : tagPart;

            if (hasLeadingSlash && !isClosing) isClosing = true;

            if (!tagName.isEmpty() && isKnownTag(tagName)) {
                if (isClosing) {
                    activeTags.remove(getTagKey(tagName));
                } else {
                    String[] parts = tagName.split(":", 2);
                    String key = parts[0];
                    String data = parts.length > 1 ? parts[1] : "";
                    activeTags.put(key, data);
                }
            }
        }
    }

    private String getTagKey(@NotNull String tagName) {
        return tagName.split(":", 2)[0];
    }

    protected boolean isKnownTag(@NotNull String tag) {
        String tagKey = getTagKey(tag);
        return formatters.containsKey(tagKey);
    }

    private @NotNull List<AbstractFormatter> getTagFormatters(@NotNull HashMap<String, String> activeTags) {
        List<AbstractFormatter> tagFormatters = new ArrayList<>(globalFormatters);

        for (String formatterTag : formatters.keySet()) {
            if (activeTags.containsKey(formatterTag)) {
                AbstractFormatter formatter = formatters.get(formatterTag);
                if (formatter != null) {
                    tagFormatters.add(formatter.createFromData(activeTags.get(formatterTag)));
                }
            }
        }

        return tagFormatters;
    }

    private @NotNull Component buildComponent(@NotNull List<TextToken> tokens) {
        TextComponent.Builder builder = Component.text();

        for (TextToken token : tokens) {
            Component textComponent = Component.text(token.text);

            for (AbstractFormatter formatter : token.formatters) {
                try {
                    textComponent = formatter.format(textComponent);
                } catch (Exception e) {
                }
            }

            builder.append(textComponent);
        }

        return builder.build();
    }

    private record FormattedTextToken(
          String text,
          Style style
    ) {}

    private record TextToken(
          String text,
          List<AbstractFormatter> formatters
    ) {}
}