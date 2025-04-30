package xyz.bitsquidd.bits.lib.sendable.text.decorator.examples;

import xyz.bitsquidd.bits.lib.command.CommandReturnType;

public class CommandReturnDecorator extends TextTagDecorator {
    private final CommandReturnType commandReturnType;

    public CommandReturnDecorator(CommandReturnType commandReturnType) {
        super();
        this.commandReturnType = commandReturnType;
    }

    @Override
    public int getColor() {
        return commandReturnType.color;
    }

    @Override
    public String getPrefix() {
        return commandReturnType.icon;
    }
}
