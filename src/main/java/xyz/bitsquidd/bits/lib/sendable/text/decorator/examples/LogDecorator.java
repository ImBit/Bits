package xyz.bitsquidd.bits.lib.sendable.text.decorator.examples;

import xyz.bitsquidd.bits.lib.logging.LogType;


public class LogDecorator extends TextTagDecorator {
    private final LogType logType;

    public LogDecorator(LogType logType) {
        this.logType = logType;
    }


    @Override
    public int getColor() {
        return logType.color;
    }

    @Override
    public String getPrefix() {
        return logType.icon;
    }
}
