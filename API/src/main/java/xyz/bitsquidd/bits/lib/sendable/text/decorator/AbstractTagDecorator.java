package xyz.bitsquidd.bits.lib.sendable.text.decorator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.helper.component.ComponentHelper;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.formatter.AbstractFormatter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractTagDecorator implements ITextDecorator {
    protected final Set<AbstractFormatter> globalFormatters = new LinkedHashSet<>();
    protected final Map<String, AbstractFormatter> formatters = new LinkedHashMap<>();

    private static final Pattern TAG_PATTERN = Pattern.compile("<([^>]+)>");

    @Override
    public @NotNull Component format(@NotNull Component component, @NotNull Locale locale) {
        List<TextComponent> flattenedComponents = ComponentHelper.flatten(component, locale);

        TagContext context = new TagContext();
        List<Component> processedComponents = new ArrayList<>();

        for (TextComponent textComponent : flattenedComponents) {
            processedComponents.addAll(processComponentWithTags(textComponent, context));
        }

        return Component.empty().children(processedComponents);
    }

    private @NotNull List<Component> processComponentWithTags(
          @NotNull TextComponent textComponent,
          @NotNull TagContext context
    ) {

        String content = textComponent.content();

        if (content.isEmpty()) return List.of(textComponent);

        List<Component> components = new ArrayList<>();
        Matcher matcher = TAG_PATTERN.matcher(content);
        int lastEnd = 0;

        while (matcher.find()) {
            String textBefore = content.substring(lastEnd, matcher.start());
            if (!textBefore.isEmpty()) {
                components.add(createStyledComponent(textBefore, textComponent.style(), context.activeTags));
            }

            String tagContent = matcher.group(1);
            processTagOperations(tagContent, context.activeTags);

            lastEnd = matcher.end();
        }

        String remainingText = content.substring(lastEnd);
        if (!remainingText.isEmpty()) {
            components.add(createStyledComponent(remainingText, textComponent.style(), context.activeTags));
        }

        if (components.isEmpty() && context.activeTags.isEmpty()) return List.of();

        if (components.isEmpty()) {
            return List.of(createStyledComponent(content, textComponent.style(), context.activeTags));
        }

        return components;
    }

    private @NotNull Component createStyledComponent(
          @NotNull String text,
          @NotNull net.kyori.adventure.text.format.Style baseStyle,
          @NotNull Map<String, String> activeTags
    ) {

        Component component = Component.text(text, baseStyle);

        for (AbstractFormatter formatter : globalFormatters) {
            component = formatter.format(component);
        }

        for (Map.Entry<String, String> tagEntry : activeTags.entrySet()) {
            AbstractFormatter formatter = formatters.get(tagEntry.getKey());
            if (formatter != null) {
                AbstractFormatter instanceFormatter = formatter.createFromData(tagEntry.getValue());
                component = instanceFormatter.format(component);
            }
        }

        return component;
    }

    private void processTagOperations(@NotNull String tagContent, @NotNull Map<String, String> activeTags) {
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

            String tagKey = extractTagKey(tagName);
            if (!isKnownTag(tagKey)) continue;

            if (isClosing) {
                activeTags.remove(tagKey);
            } else {
                String[] parts = tagName.split(":", 2);
                String data = parts.length > 1 ? parts[1] : "";
                activeTags.put(tagKey, data);
            }
        }
    }

    private @NotNull String extractTagKey(@NotNull String tagName) {
        return tagName.split(":", 2)[0];
    }

    protected boolean isKnownTag(@NotNull String tagKey) {
        return formatters.containsKey(tagKey);
    }

    private static class TagContext {
        final Map<String, String> activeTags = new LinkedHashMap<>();
    }

}