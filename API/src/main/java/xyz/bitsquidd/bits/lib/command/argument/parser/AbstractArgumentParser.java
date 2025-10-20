package xyz.bitsquidd.bits.lib.command.argument.parser;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@NullMarked
public abstract class AbstractArgumentParser<I, O> {
    private final TypeSignature typeSignature;  // The signature methods expect to receive
    private final ArgumentType<I> argumentType; // The brigadier type used for clientside highlighting.
    private final Class<I> inputClass;
    private final Class<O> outputClass;
    private final String argumentName;

    public AbstractArgumentParser(TypeSignature typeSignature, ArgumentType<I> argumentType, Class<I> inputClass, Class<O> outputClass, String argumentName) {
        this.typeSignature = typeSignature;
        this.argumentType = argumentType;

        this.inputClass = inputClass;
        this.outputClass = outputClass;
        this.argumentName = argumentName;
    }

    public abstract @NotNull O parse(@NotNull I input, BitsCommandContext context) throws CommandSyntaxException;

    protected List<String> getSuggestions(BitsCommandContext context) {
        return List.of();
    }

    public final SuggestionProvider<CommandSourceStack> getSuggestionProvider(@Nullable SuggestionProvider<CommandSourceStack> superProvider) {
        return (context, builder) -> {
            BitsCommandContext bitsCtx = BitsConfig.getCommandManager().createContext(context.getSource());
            List<String> suggestions = getSuggestions(bitsCtx);
            String remaining = builder.getRemaining().toLowerCase();

            for (String suggestion : suggestions) {
                if (suggestion.toLowerCase().startsWith(remaining)) {
                    builder.suggest(suggestion);
                }
            }

            CompletableFuture<Suggestions> customProvider = builder.buildFuture();
            if (superProvider != null) {
                customProvider = customProvider.thenCompose(unused -> {
                    try {
                        return superProvider.getSuggestions(context, builder);
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            return customProvider;
        };
    }

    public TypeSignature getTypeSignature() {
        return typeSignature;
    }

    public ArgumentType<I> getArgumentType() {
        return argumentType;
    }

    public Class<I> getInputClass() {
        return inputClass;
    }

    public Class<O> getOutputClass() {
        return outputClass;
    }

    public String getArgumentName() {
        return argumentName;
    }

}
