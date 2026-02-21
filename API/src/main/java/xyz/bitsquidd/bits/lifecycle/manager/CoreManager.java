/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lifecycle.manager;

/**
 * A template class for managers with lifecycle methods.
 * This is intended to be implemented by any manager that needs to perform actions at
 * specific points in the server lifecycle, such as startup, round initialisation,
 * round cleanup, and shutdown.
 */
public interface CoreManager {

    /**
     * Should be called when the server is starting up, before anything else.
     */
    default void startup() {}


    /**
     * Should be called at the start of a round or significant occasion.
     */
    default void initialise() {}

    /**
     * Should be called at the end of every round or significant occasion.
     */
    default void cleanup() {}

    /**
     * Called when the server is shutting down, after everything else.
     */
    default void shutdown() {}

}
