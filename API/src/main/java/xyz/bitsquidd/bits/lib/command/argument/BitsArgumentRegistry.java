package xyz.bitsquidd.bits.lib.command.argument;

import com.mojang.brigadier.arguments.*;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.parser.impl.*;
import xyz.bitsquidd.bits.lib.command.argument.parser.impl.generic.GenericEnumParser;
import xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive.*;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.lib.type.GreedyString;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NullMarked
public class BitsArgumentRegistry {
    private static @Nullable BitsArgumentRegistry instance;

    private final Map<TypeSignature<?>, AbstractArgumentParser<?>> parsers = new HashMap<>();


    public BitsArgumentRegistry() {
        if (instance != null) throw new IllegalStateException("ArgumentRegistry has already been initialized.");
        instance = this;

        List<AbstractArgumentParser<?>> initialParsers = new ArrayList<>(initialiseDefaultParsers());
        initialParsers.addAll(initialiseParsers());
        initialParsers.forEach(parser -> parsers.put(parser.getTypeSignature(), parser));
    }

    public static BitsArgumentRegistry getInstance() {
        if (instance == null) throw new IllegalStateException("ArgumentRegistry has not been initialized yet.");
        return instance;
    }

    private @Nullable ArgumentType<?> toArgumentType(TypeSignature<?> inputType) {
        Class<?> clazz = inputType.toRawType();
        if (clazz == Integer.class || clazz == int.class) {
            return IntegerArgumentType.integer();
        } else if (clazz == Double.class || clazz == double.class) {
            return DoubleArgumentType.doubleArg();
        } else if (clazz == Float.class || clazz == float.class) {
            return FloatArgumentType.floatArg();
        } else if (clazz == Long.class || clazz == long.class) {
            return LongArgumentType.longArg();
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return BoolArgumentType.bool();
        } else if (clazz == GreedyString.class) {
            return StringArgumentType.greedyString();
        } else if (clazz == String.class) {
            return StringArgumentType.string();
        } else if (clazz == EntitySelector.class) {
            // Note net.minecraft.world.entity.EntitySelector and net.minecraft.commands.arguments.selector.EntitySelector are different things.
            // Our parsers expect a result in net.minecraft.commands.arguments.selector.EntitySelector.
            return EntityArgument.entities(); // TODO, in the future, we could consider refining this to be single/multiple/entity/player selectors. For now the parser should filter this.
        }

        return null;
    }

    private List<AbstractArgumentParser<?>> initialiseDefaultParsers() {
        return List.of(
              new BooleanArgumentParser(),
              new DoubleArgumentParser(),
              new FloatArgumentParser(),
              new IntegerArgumentParser(),
              new LongArgumentParser(),
              new StringArgumentParser(),
              new EntitySelectorArgumentParser(),

              new GreedyStringArgumentParser(),
              new PlayerCollectionArgumentParser(),
              new PlayerSingleArgumentParser(),
              new WorldArgumentParser(),
              new LocationArgumentParser(),
              new BlockPosArgumentParser()
        );
    }

    protected List<AbstractArgumentParser<?>> initialiseParsers() {
        // Override to add custom parsers
        return List.of();
    }

    /**
     * Retrieves the appropriate parser for the given type signature.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public AbstractArgumentParser<?> getParser(TypeSignature<?> typeSignature) {
        // We could consider implementing some form of search for inherited types.
        // This probably shouldn't be implemented as it'll cause type inconsistencies with functions.
        // Developers should design their command functions accordingly to use the lowest available type.
        AbstractArgumentParser<?> parser = parsers.get(typeSignature);

        // If no parser found, we allow generic enums to be parsed.
        if (parser == null) {
            Class<?> rawType = typeSignature.toRawType();
            if (rawType.isEnum()) {
                Class<? extends Enum> enumClass = (Class<? extends Enum>)rawType;
                return new GenericEnumParser<>(enumClass);
            }

            BitsConfig.getPlugin().getLogger().severe("No parser registered for type: " + typeSignature);
            return new VoidParser();
        }

        return parser;
    }

    public List<BrigadierArgumentMapping> getArgumentTypeContainer(AbstractArgumentParser<?> parser, String baseName) {
        List<BrigadierArgumentMapping> holders = new ArrayList<>();
        List<InputTypeContainer> inputTypes = parser.getInputTypes();

        // Break down the type signature into its primitives.
        for (int i = 0; i < inputTypes.size(); i++) {
            InputTypeContainer nestedTypeSigature = inputTypes.get(i);
            // Get the command parser required for this input type
            AbstractArgumentParser<?> nestedParser = getParser(nestedTypeSigature.typeSignature());

            boolean handled = false;
            // If its a primitive, we can directly add it
            if (nestedParser.getInputTypes().size() == 1) {
                InputTypeContainer inputType = nestedParser.getInputTypes().getFirst();
                ArgumentType<?> brigadierType = toArgumentType(inputType.typeSignature());

                if (brigadierType != null) {
                    String argumentName = inputTypes.size() > 1
                                          ? baseName + "_" + nestedTypeSigature.typeName()
                                          : baseName;

                    holders.add(new BrigadierArgumentMapping(
                          brigadierType,
                          nestedParser.getTypeSignature(),
                          argumentName
                    ));
                    handled = true;
                }
            }

            if (!handled) {
                // Recurse into non-primitive parsers
                holders.addAll(getArgumentTypeContainer(nestedParser, baseName + "_" + nestedTypeSigature.typeName()));
            }
        }

        return holders;
    }

    // Parses primitives into required objects needed by the parser
    public Object parseArguments(AbstractArgumentParser<?> parser, List<Object> primitiveList, BitsCommandContext ctx) throws CommandParseException {
        List<InputTypeContainer> inputTypes = parser.getInputTypes();

        // If the input size is 1, we can directly parse it
        if (inputTypes.size() == 1) return parser.parse(primitiveList, ctx);

        List<Object> parsedObjects = new ArrayList<>();

        for (InputTypeContainer inputType : inputTypes) {
            AbstractArgumentParser<?> nestedParser = getParser(inputType.typeSignature());

            int requiredSize = nestedParser.getInputTypes().size();
            if (primitiveList.size() < requiredSize) throw new CommandParseException("Not enough arguments for " + inputType.typeName());

            ArrayList<Object> inputObjects = new ArrayList<>(primitiveList.subList(0, requiredSize));
            primitiveList = new ArrayList<>(primitiveList.subList(requiredSize, primitiveList.size()));

            // Recursively parse the primitives with the appropriate parser
            Object parsedObject = parseArguments(nestedParser, inputObjects, ctx);
            parsedObjects.add(parsedObject);
        }

        // Now that we have all our parsed objects, we can pass them to the main parser
        return parser.parse(parsedObjects, ctx);
    }


}
