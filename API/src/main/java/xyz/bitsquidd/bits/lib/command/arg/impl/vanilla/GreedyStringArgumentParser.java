package xyz.bitsquidd.bits.lib.command.arg.impl.vanilla;

import com.mojang.brigadier.arguments.StringArgumentType;

import xyz.bitsquidd.bits.lib.command.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.arg.parser.VanillaArgumentParser;

public class GreedyStringArgumentParser extends VanillaArgumentParser<String> {
    public GreedyStringArgumentParser() {
        super(TypeSignature.of(String.class), StringArgumentType.greedyString());
    }

}