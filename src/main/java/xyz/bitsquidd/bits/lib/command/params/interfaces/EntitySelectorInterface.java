package xyz.bitsquidd.bits.lib.command.params.interfaces;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.SelectorParseException;

import java.util.List;

public interface EntitySelectorInterface {
    List<Entity> parseEntities(String selectorString, CommandContext context) throws SelectorParseException;
    List<Player> parsePlayers(String selectorString, CommandContext context) throws SelectorParseException;
    boolean isSelector(String input);
    Entity parseSingleEntity(String selectorString, CommandContext context) throws SelectorParseException;
    Player parseSinglePlayer(String selectorString, CommandContext context) throws SelectorParseException;
    boolean isValidForPermissions(String selectorString, CommandContext context);
}