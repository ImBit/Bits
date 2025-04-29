package xyz.bitsquidd.bits.lib.command;

@FunctionalInterface
public interface CommandHandler {
    void handle(CommandContext context);
}