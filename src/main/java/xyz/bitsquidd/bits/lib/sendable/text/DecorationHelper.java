package xyz.bitsquidd.bits.lib.sendable.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import xyz.bitsquidd.bits.lib.component.ComponentHelper;

public class DecorationHelper {

    public static Component clearDecorations(Component component) {
        return ComponentHelper.styleAll(component, Style.empty());
    }

}
