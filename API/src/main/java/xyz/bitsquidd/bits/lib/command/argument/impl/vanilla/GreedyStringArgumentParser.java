package xyz.bitsquidd.bits.lib.command.argument.impl.vanilla;

import com.mojang.brigadier.arguments.StringArgumentType;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.VanillaArgumentParser;

public class GreedyStringArgumentParser extends VanillaArgumentParser<String> {
    public GreedyStringArgumentParser() {
        super(TypeSignature.of(String.class), StringArgumentType.greedyString(), "String...");
    }

}