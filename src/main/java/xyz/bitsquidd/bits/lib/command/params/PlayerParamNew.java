package xyz.bitsquidd.bits.lib.command.params;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.CommandContextNew;
import xyz.bitsquidd.bits.lib.command.exceptions.ParamParseException;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerParamNew implements CommandParamNew<Player> {
    public static final PlayerParamNew INSTANCE = new PlayerParamNew();

    @Override
    public Player parse(CommandContextNew context, int startIndex) throws ParamParseException {
        Player player = Bukkit.getPlayer(context.getArg(startIndex));
        if (player == null) {
            throw new ParamParseException("Player not found: " + context.getArg(startIndex));
        }
        return player;
    }

    @Override
    public boolean canParseArg(CommandContextNew context, int argIndex) {
        try {
            LogController.warning("PLAYER TRUE");
            return true;
        } catch (Exception e) {
            LogController.warning("PLAYER FALSE");
            return false;
        }
    }

    @Override
    public List<String> tabComplete(CommandContextNew context) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(context.getLastArg()))
                .collect(Collectors.toList());
    }

    @Override
    public String getTypeName() {
        return "player";
    }
}