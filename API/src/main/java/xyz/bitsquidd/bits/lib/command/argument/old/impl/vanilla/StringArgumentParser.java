package xyz.bitsquidd.bits.lib.command.argument.old.impl.vanilla;

import com.mojang.brigadier.arguments.StringArgumentType;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.old.parser.VanillaArgumentParser;

public class StringArgumentParser extends VanillaArgumentParser<String> {
    public StringArgumentParser() {
        super(TypeSignature.of(String.class), StringArgumentType.string(), "String");
    }

}