package xyz.bitsquidd.bits.lib.sendable.text.decorator;

import net.kyori.adventure.text.Component;

import java.util.Locale;

public interface ITextDecorator {
    Component format(Component component, Locale locale);

}
