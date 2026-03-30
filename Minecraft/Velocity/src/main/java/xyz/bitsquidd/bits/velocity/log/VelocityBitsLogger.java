/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.velocity.log;

import xyz.bitsquidd.bits.log.BasicLogger;


public class VelocityBitsLogger extends BasicLogger {
    public VelocityBitsLogger(org.slf4j.Logger slf4j, LogFlags flags) {
        super(slf4j, flags);
    }

}