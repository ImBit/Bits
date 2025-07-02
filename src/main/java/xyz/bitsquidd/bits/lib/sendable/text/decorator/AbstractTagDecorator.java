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
        if (content.isEmpty()) return Component.empty();
        List<TextToken> tokens = tokenize(content);
        return buildComponent(tokens);
    }

    private @NotNull List<TextToken> tokenize(@NotNull String content) {
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
                tokens.add(new TextToken(currentText.toString(), new LinkedHashMap<>(activeTags)));
                currentText = new StringBuilder();
            }

            processTagOperations(tagContent, activeTags);
            lastEnd = matcher.end();
        }

        if (lastEnd < content.length()) currentText.append(content.substring(lastEnd));
        if (!currentText.isEmpty()) tokens.add(new TextToken(currentText.toString(), new LinkedHashMap<>(activeTags)));

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
        return tagName.split(":", 2)[0]; // For closing tags, data is optional. We could have "c" or "c:data", both are valid.
    }

    protected boolean isKnownTag(@NotNull String tag) {
        String tagKey = getTagKey(tag);
        return formatters.containsKey(tagKey);
    }

    private @NotNull Component buildComponent(@NotNull List<TextToken> tokens) {
        TextComponent.Builder builder = Component.text();

        for (TextToken token : tokens) {
            Component textComponent = Component.text(token.text);
            List<AbstractFormatter> applicableFormatters = getApplicableFormatters(token.activeTags);

            for (AbstractFormatter formatter : applicableFormatters) {
                try {
                    textComponent = formatter.format(textComponent);
                } catch (Exception e) {}
            }

            builder.append(textComponent);
        }

        return builder.build();
    }

    private @NotNull List<AbstractFormatter> getApplicableFormatters(@NotNull HashMap<String, String> activeTags) {
        List<AbstractFormatter> applicable = new ArrayList<>(globalFormatters);

        for (String formatterTag : formatters.keySet()) {
            if (activeTags.containsKey(formatterTag)) {
                AbstractFormatter formatter = formatters.get(formatterTag);
                if (formatter != null) {
                    applicable.add(formatter.createFromData(activeTags.get(formatterTag)));
                }
            }
        }

        return applicable;
    }

    private record TextToken(
          String text,
          HashMap<String, String> activeTags
    ) {}
}