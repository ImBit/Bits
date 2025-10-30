package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;
import java.util.function.Supplier;

public final class PlayerSingleArgumentParser extends AbstractArgumentParserNew<@NotNull Player> {

    public PlayerSingleArgumentParser() {
        super(TypeSignature.of(Player.class), "Player");
    }

    @Override
    public @NotNull Player parse(@NotNull List<Object> inputObjects, @NotNull BitsCommandContext ctx) throws CommandParseException {
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
//        Player player = Bukkit.getPlayer(inputString);
//        if (player == null) throw new CommandParseException("Player not found: " + inputString);
//        return player;
    }

    @Override
    public @NotNull List<InputTypeContainer> getInputTypes() {
        return List.of(new InputTypeContainer(TypeSignature.of(EntitySelector.class), getArgumentName()));
    }

    @Override
    public @Nullable Supplier<List<String>> getSuggestions() {
        return () -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

}
