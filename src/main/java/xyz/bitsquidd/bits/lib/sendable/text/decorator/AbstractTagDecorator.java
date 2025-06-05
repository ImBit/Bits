package xyz.bitsquidd.bits.lib.sendable.text.decorator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.component.ComponentHelper;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters.AbstractFormatter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractTagDecorator implements ITextDecorator {

    protected final LinkedHashSet<AbstractFormatter> globalFormatters = new LinkedHashSet<>();
    protected final LinkedHashMap<String, AbstractFormatter> formatters = new LinkedHashMap<>();
    private static final Pattern TAG_PATTERN = Pattern.compile("<([^>]+)>");

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
        Set<String> activeTags = new LinkedHashSet<>();

        Matcher matcher = TAG_PATTERN.matcher(content);
        int lastEnd = 0;

        while (matcher.find()) {
            String beforeTag = content.substring(lastEnd, matcher.start());
            currentText.append(beforeTag);

            String tagContent = matcher.group(1);

            if (!currentText.isEmpty()) {
                tokens.add(new TextToken(currentText.toString(), new LinkedHashSet<>(activeTags)));
                currentText = new StringBuilder();
            }

            processTagOperations(tagContent, activeTags);

            lastEnd = matcher.end();
        }

        if (lastEnd < content.length()) {
            currentText.append(content.substring(lastEnd));
        }

        if (!currentText.isEmpty()) {
            tokens.add(new TextToken(currentText.toString(), new LinkedHashSet<>(activeTags)));
        }

        return tokens;
    }

    private void processTagOperations(@NotNull String tagContent, @NotNull Set<String> activeTags) {
        boolean hasLeadingSlash = tagContent.startsWith("/");
        if (hasLeadingSlash) {
            tagContent = tagContent.substring(1);
        }

        String[] tagParts = tagContent.split(",");

        for (String tagPart : tagParts) {
            tagPart = tagPart.trim();
            if (tagPart.isEmpty()) continue;

            boolean isClosing = tagPart.startsWith("/");
            String tagName = isClosing ? tagPart.substring(1).trim() : tagPart;

            if (hasLeadingSlash && !isClosing) {
                isClosing = true;
            }

            if (!tagName.isEmpty() && isKnownTag(tagName)) {
                if (isClosing) {
                    activeTags.remove(tagName);
                } else {
                    activeTags.add(tagName);
                }
            }
        }
    }

    private boolean isKnownTag(@NotNull String tag) {
        // Check if this tag appears in any formatter key
        for (String formatterKey : formatters.keySet()) {
            String[] formatterTags = formatterKey.split("\\|");
            for (String formatterTag : formatterTags) {
                if (formatterTag.trim().equals(tag)) {
                    return true;
                }
            }
        }
        return false;
    }

    private @NotNull Component buildComponent(@NotNull List<TextToken> tokens) {
        TextComponent.Builder builder = Component.text();

        for (TextToken token : tokens) {
            Component textPlaceholder = Component.text(token.text);

            List<AbstractFormatter> applicableFormatters = getApplicableFormattersInOrder(token.activeTags);

            for (AbstractFormatter formatter : applicableFormatters) {
                textPlaceholder = formatter.format(textPlaceholder);
            }

            builder.append(textPlaceholder);
        }

        return builder.build();
    }

    private @NotNull List<AbstractFormatter> getApplicableFormattersInOrder(@NotNull Set<String> activeTags) {
        List<AbstractFormatter> applicableFormatters = new ArrayList<>(globalFormatters);

        for (Map.Entry<String, AbstractFormatter> entry : formatters.entrySet()) {
            String formatterKey = entry.getKey();
            AbstractFormatter formatter = entry.getValue();

            if (shouldApplyFormatter(formatterKey, activeTags)) {
                applicableFormatters.add(formatter);
            }
        }

        return applicableFormatters;
    }

    private boolean shouldApplyFormatter(@NotNull String formatterKey, @NotNull Set<String> activeTags) {
        String[] requiredTags = formatterKey.split("\\|");

        for (String requiredTag : requiredTags) {
            if (activeTags.contains(requiredTag.trim())) {
                return true;
            }
        }

        return false;
    }

    private record TextToken(String text, Set<String> activeTags) {}
}