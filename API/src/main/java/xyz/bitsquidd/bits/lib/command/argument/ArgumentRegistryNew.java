package xyz.bitsquidd.bits.lib.command.argument;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.impl.GenericEnumParser;
import xyz.bitsquidd.bits.lib.command.argument.impl.PlayerCollectionArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.impl.PlayerSingleArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.impl.WorldArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.impl.primitive.*;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO:
//  Start at the highest level of inheritance, if there is nothing, go down a level to see if its present
@NullMarked
public class ArgumentRegistryNew {
    private static @Nullable ArgumentRegistryNew instance;

    private final Map<TypeSignature, AbstractArgumentParserNew<?>> parsers = new HashMap<>();


    public ArgumentRegistryNew() {
        if (instance != null) throw new IllegalStateException("ArgumentRegistry has already been initialized.");
        instance = this;

        List<AbstractArgumentParserNew<?>> initialParsers = new ArrayList<>(initialiseDefaultParsers());
        initialParsers.addAll(initialiseParsers());
        initialParsers.forEach(parser -> parsers.put(parser.getTypeSignature(), parser));
    }

    public static ArgumentRegistryNew getInstance() {
        if (instance == null) throw new IllegalStateException("ArgumentRegistry has not been initialized yet.");
        return instance;
    }

    private List<AbstractArgumentParserNew<?>> initialiseDefaultParsers() {
        return List.of(
              new BooleanArgumentParser(),
              new DoubleArgumentParser(),
              new FloatArgumentParser(),
              new IntegerArgumentParser(),
              new LongArgumentParser(),
              new StringArgumentParser(),

              new GreedyStringArgumentParser(),
              new GenericEnumParser(),
              new PlayerCollectionArgumentParser(),
              new PlayerSingleArgumentParser(),
              new WorldArgumentParser()
        );
    }

    protected List<AbstractArgumentParserNew<?>> initialiseParsers() {
        // Override to add custom parsers
        return List.of();
    }

    public AbstractArgumentParserNew<?> getParser(TypeSignature<?> typeSignature) {
        AbstractArgumentParserNew<?> parser = parsers.get(typeSignature);
        if (parser == null) throw new CommandParseException("No parser registered for type: " + typeSignature.toRawType().getSimpleName());
        return parser;
    }

}
