package xyz.bitsquidd.bits.lib.command;

@FunctionalInterface
public interface CommandHandlerNew {
    void execute(CommandContextNew context);
}