package xyz.bitsquidd.bits.lib.command.newe.core;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;

public class BRequiredArgumentBuilder<S, T> extends ArgumentBuilder<S, BRequiredArgumentBuilder<S, T>> {
    private final String name;
    private final ArgumentType<T> type;
    private SuggestionProvider<S> suggestionsProvider = null;

    private BRequiredArgumentBuilder(String name, ArgumentType<T> type) {
        this.name = name;
        this.type = type;
    }

    public static <S, T> BRequiredArgumentBuilder<S, T> argument(String name, ArgumentType<T> type) {
        return new BRequiredArgumentBuilder<S, T>(name, type);
    }

    public BRequiredArgumentBuilder<S, T> suggests(SuggestionProvider<S> provider) {
        this.suggestionsProvider = provider;
        return this.getThis();
    }

    public SuggestionProvider<S> getSuggestionsProvider() {
        return this.suggestionsProvider;
    }

    protected BRequiredArgumentBuilder<S, T> getThis() {
        return this;
    }

    public ArgumentType<T> getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public ArgumentCommandNode<S, T> build() {
        ArgumentCommandNode<S, T> result = new ArgumentCommandNode(
              this.getName(),
              this.getType(),
              this.getCommand(),
              this.getRequirement(),
              this.getRedirect(),
              this.getRedirectModifier(),
              this.isFork(),
              this.getSuggestionsProvider()
        );

        for (CommandNode<S> argument : this.getArguments()) {
            result.addChild(argument);
        }

        return result;
    }

    @Override
    public BRequiredArgumentBuilder<S, T> executes(Command<S> command) {

        return super.executes(command);
    }
}
