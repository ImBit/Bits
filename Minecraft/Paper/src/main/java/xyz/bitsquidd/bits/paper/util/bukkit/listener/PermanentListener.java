/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.util.bukkit.listener;

import org.bukkit.event.Listener;

/**
 * A special {@link Listener Listener} that should never be cancelled.
 * By default all listeners should be cancelled on {@link xyz.bitsquidd.bits.paper.lifecycle.manager.PaperManagerContainer#cleanup() PaperManagerContainer#cleanup()}.
 */
public interface PermanentListener extends Listener {
}
