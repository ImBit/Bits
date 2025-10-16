package xyz.bitsquidd.bits.lib.command.newer.arg.parser.impl;

import com.mojang.brigadier.arguments.StringArgumentType;

import xyz.bitsquidd.bits.lib.command.newer.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.newer.arg.parser.VanillaArgumentParser;

public class GreedyStringArgumentParser extends VanillaArgumentParser<String> {
    public GreedyStringArgumentParser() {
        super(TypeSignature.of(String.class), StringArgumentType.greedyString());
    }

}