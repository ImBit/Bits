package xyz.bitsquidd.bits.lib.command.newer.arg.impl;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.newer.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.newer.arg.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;

import java.util.List;
import java.util.stream.Stream;

@NullMarked
public class EnumArgumentParser<T extends Enum<T>> extends AbstractArgumentParser<String, T> {
    private final Class<T> enumClass;

    private final DynamicCommandExceptionType ERROR_NOT_VALID;

    public EnumArgumentParser(Class<T> enumClass) {
        super(TypeSignature.of(enumClass), StringArgumentType.string(), String.class, enumClass);
        this.enumClass = enumClass;

        if (!enumClass.isEnum()) throw new IllegalArgumentException("Provided class " + enumClass.getName() + " is not an enum!");

        ERROR_NOT_VALID = new DynamicCommandExceptionType(
              name -> new LiteralMessage("<b>" + name + "</b> is not a valid <b>" + enumClass.getSimpleName() + "</b>.")
        );
    }

    @Override
    public @NotNull T parse(String input, BitsCommandContext context) throws CommandSyntaxException {
        T enumValue;
        try {
            enumValue = Enum.valueOf(enumClass, input);
        } catch (IllegalArgumentException e) {
            throw ERROR_NOT_VALID.create(input);
        }

        return enumValue;
    }

    @Override
    protected List<String> getSuggestions(BitsCommandContext context) {
        return enumClass.isEnum() ? Stream.of(enumClass.getEnumConstants()).map(Enum::name).toList() : List.of();
    }

}