package xyz.bitsquidd.bits.lib.command.argument.parser;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.util.List;

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

    public final SuggestionProvider<CommandSourceStack> getSuggestionProvider() {
        BitsConfig.getPlugin().getLogger().info("getting suggestion provider for " + this.typeSignature);
        return (context, builder) -> {
            BitsCommandContext bitsCtx = new BitsCommandContext(context.getSource());
            List<String> suggestions = getSuggestions(bitsCtx);
            String remaining = builder.getRemaining().toLowerCase();

            BitsConfig.getPlugin().getLogger().info("suggestions: " + suggestions);

            for (String suggestion : suggestions) {
                if (suggestion.toLowerCase().startsWith(remaining)) {
                    builder.suggest(suggestion);
                    BitsConfig.getPlugin().getLogger().info("suggesting: " + suggestion);
                }
            }
            return builder.buildFuture();
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
