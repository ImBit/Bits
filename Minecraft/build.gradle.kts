/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */


subprojects {
    dependencies {
        shade(project(":Minecraft"))
    }
}

allprojects {
    dependencies {
        shade(project(":API"))
    }
}