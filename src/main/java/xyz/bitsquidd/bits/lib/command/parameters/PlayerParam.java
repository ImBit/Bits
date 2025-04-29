package xyz.bitsquidd.bits.lib.command.parameters;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exception.ParamParseException;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerParam implements CommandParam<Player> {
    public static final PlayerParam INSTANCE = new PlayerParam();
    private static final boolean DEBUG = true;

    @Override
    public Player parse(CommandContext context, String arg) throws ParamParseException {
        if (DEBUG) {
            LogController.error("PlayerParam parsing: " + arg);
        }

        Player player = Bukkit.getPlayer(arg);
        if (player == null) {
            throw new ParamParseException("Player not found: " + arg);
        }
        return player;
    }

    @Override
    public List<String> tabComplete(CommandContext context, String current) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(current.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String getTypeName() {
        return "player";
    }
}