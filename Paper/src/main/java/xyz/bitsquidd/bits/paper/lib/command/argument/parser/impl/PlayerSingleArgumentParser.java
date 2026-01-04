package xyz.bitsquidd.bits.paper.lib.command.argument.parser.impl;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

import java.util.List;
import java.util.function.Supplier;

public final class PlayerSingleArgumentParser extends AbstractArgumentParser<Player> {

    public PlayerSingleArgumentParser() {
        super(TypeSignature.of(Player.class), "Player");
    }

    @Override
    public Player parse(List<Object> inputObjects, BitsCommandContext<?> ctx) throws CommandParseException {
        EntitySelector entitySelctor = singletonInputValidation(inputObjects, EntitySelector.class);

        try {
            return entitySelctor.findPlayers((CommandSourceStack)ctx.getBrigadierContext().getSource())
              .stream()
              .map(playerEntity -> playerEntity.getBukkitEntity().getPlayer())
              .findFirst()
              .get();
        } catch (Exception e) {
            throw new CommandParseException("Player not found!");
        }
    }

    @Override
    public List<InputTypeContainer> getInputTypes() {
        return List.of(new InputTypeContainer(TypeSignature.of(EntitySelector.class), getArgumentName()));
    }

    @Override
    public Supplier<List<String>> getSuggestions() {
        return () -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

}
