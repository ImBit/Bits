import xyz.bitsquidd.util.includeLibrary
import xyz.bitsquidd.util.shadeApi

/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */


dependencies {
    shadeApi(project(":api"))

    includeLibrary(rootProject.libs.brigadier)
}