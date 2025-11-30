package xyz.bitsquidd.bits.paper.libs.command;

import org.bukkit.Bukkit;

import xyz.bitsquidd.bits.lib.command.BitsCommandManager;
import xyz.bitsquidd.bits.paper.PaperBitsConfig;

public abstract class PaperBitsCommandManager extends BitsCommandManager {

    @Override
    protected void executeCommand(boolean isAsync, Runnable commandExecution) {
        if (isAsync) {
            Bukkit.getScheduler().runTaskAsynchronously(((PaperBitsConfig)PaperBitsConfig.get()).getPlugin(), commandExecution);
        } else {
            Bukkit.getScheduler().runTask(((PaperBitsConfig)PaperBitsConfig.get()).getPlugin(), commandExecution);
        }
    }

}
