package xyz.bitsquidd.bits.lib.command.argument;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.impl.GenericEnumParser;
import xyz.bitsquidd.bits.lib.command.argument.impl.PlayerCollectionArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.impl.PlayerSingleArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.impl.WorldArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.impl.primitive.*;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.command.argument.parser.ArgumentTypeRegistry;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

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

    public List<ArgumentTypeContainer> getArgumentTypes(TypeSignature<?> typeSignature) {
        List<ArgumentTypeContainer> argumentTypes = new ArrayList<>();

        AbstractArgumentParserNew<?> parser = getParser(typeSignature);

        // Break down the type signature into its primitives.
        parser.getInputTypes().forEach(nestedTypeSigature -> {
            AbstractArgumentParserNew<?> nestedParser = parsers.get(nestedTypeSigature.typeSignature());
            if (nestedParser instanceof PrimitiveArgumentParserNew<?> primitiveArg) {
                argumentTypes.add(new ArgumentTypeContainer(
                      ArgumentTypeRegistry.getArgumentType(nestedTypeSigature.typeSignature().toRawType()),
                      primitiveArg.getArgumentName() // TODO get the names of non-primitive parsers here
                ));
            } else {
                argumentTypes.addAll(getArgumentTypes(nestedTypeSigature.typeSignature()));
            }
        });

        return argumentTypes;
    }

    // Note the primitive list will be mutated.
    public <T> T parseArguments(AbstractArgumentParserNew<T> parser, ArrayList<Object> primitiveList, BitsCommandContext ctx) throws CommandParseException {
        List<InputTypeContainer> inputTypes = parser.getInputTypes();

        // If the input size is 1, we can directly parse it
        if (inputTypes.size() == 1) return parser.parse(primitiveList, ctx);

        List<Object> parsedObjects = new ArrayList<>();

        for (InputTypeContainer inputType : inputTypes) {
            AbstractArgumentParserNew<?> nestedParser = parsers.get(inputType.typeSignature());

            int requiredSize = nestedParser.getInputTypes().size();
            if (primitiveList.size() < requiredSize) {
                throw new CommandParseException("Not enough arguments for " + inputType.typeName());
            }

            ArrayList<Object> inputObjects = new ArrayList<>(primitiveList.subList(0, requiredSize));
            primitiveList = new ArrayList<>(primitiveList.subList(requiredSize, primitiveList.size()));

            Object parsedObject = parseArguments(nestedParser, inputObjects, ctx);
            parsedObjects.add(parsedObject);
        }

        // Now that we have all our parsed objects, we can pass them to the main parser
        return parser.parse(parsedObjects, ctx);
    }


}
