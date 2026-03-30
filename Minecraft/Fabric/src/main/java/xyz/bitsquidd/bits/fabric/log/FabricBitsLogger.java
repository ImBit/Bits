/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.fabric.log;

import xyz.bitsquidd.bits.log.BasicLogger;

public class FabricBitsLogger extends BasicLogger {
    public FabricBitsLogger(org.slf4j.Logger slf4j, LogFlags flags) {
        super(slf4j, flags);
    }

}