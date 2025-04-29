package xyz.bitsquidd.bits.lib.command;

@FunctionalInterface
public interface CommandHandler {
    void execute(CommandContext context);
}