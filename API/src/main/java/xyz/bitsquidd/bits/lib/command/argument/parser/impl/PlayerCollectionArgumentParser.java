package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class PlayerCollectionArgumentParser extends AbstractArgumentParserNew<@NotNull Collection<Player>> {
    private enum SelectorType {
        ALL("@a", ctx -> new ArrayList<>(Bukkit.getOnlinePlayers())),
        SELF("@s", ctx -> List.of(ctx.requirePlayer())),
        ;

        public final @NotNull String selector;
        public final @NotNull Function<BitsCommandContext, Collection<Player>> playerFunction;

        SelectorType(@NotNull String selector, @NotNull Function<BitsCommandContext, Collection<Player>> playerFunction) {
            this.selector = selector;
            this.playerFunction = playerFunction;
        }

        public static @Nullable SelectorType fromSelector(@NotNull String selector) {
            for (SelectorType type : values()) {
                if (type.selector.equals(selector)) {
                    return type;
                }
            }
            return null;
        }

        public @NotNull Collection<Player> get(@NotNull BitsCommandContext ctx) {
            return playerFunction.apply(ctx);
        }
    }

    public PlayerCollectionArgumentParser() {
        super(TypeSignature.of(Collection.class, Player.class), "Players");
    }

    @Override
    public @NotNull Collection<Player> parse(@NotNull List<Object> inputObjects, @NotNull BitsCommandContext ctx) throws CommandParseException {
        String inputString = singletonInputValidation(inputObjects, String.class);

        SelectorType selectorType = SelectorType.fromSelector(inputString);
        if (selectorType != null) return selectorType.get(ctx);

        Player player = Bukkit.getPlayer(inputString);
        if (player == null) throw new CommandParseException("Player not found: " + inputString);
        return List.of(player);
    }

    @Override
    public @NotNull List<InputTypeContainer> getInputTypes() {
        return List.of(new InputTypeContainer(TypeSignature.of(EntitySelector.class), getArgumentName()));
    }

    @Override
    public @Nullable Supplier<List<String>> getSuggestions() {
        List<String> suggestions = new ArrayList<>();
        suggestions.add(SelectorType.ALL.selector);
        suggestions.add(SelectorType.SELF.selector);

        suggestions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        return () -> suggestions;
    }

}
