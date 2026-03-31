import xyz.bitsquidd.relocate

/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

description = "🦑 Utility API for Bits Plugin development."

dependencies {
    shade(rootProject.libs.classgraph)

}

relocate("io.github.classgraph" to "xyz.bitsquidd.internal.classgraph")
