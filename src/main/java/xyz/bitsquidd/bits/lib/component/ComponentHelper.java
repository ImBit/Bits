package xyz.bitsquidd.bits.lib.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;

import java.util.List;
import java.util.stream.Stream;

public class ComponentHelper {

    public static Stream<Component> getChildren(Component component) {
        return component.children().stream();
    }

    public static Component styleAll(Component component, Style style) {
        List<Component> children = component.children();

        if (children.isEmpty()) {
            return component.style(style);
        } else {
            for (Component child : children) {
                styleAll(child, style);
            }

            return component.style(style);
        }
    }
}
