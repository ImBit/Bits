import xyz.bitsquidd.includeLibrary

/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */


subprojects {
    dependencies {
        includeLibrary(project(":minecraft", configuration = "shadow"))
    }
}

allprojects {
    dependencies {
        includeLibrary(project(":api", configuration = "shadow"))

        includeLibrary(rootProject.libs.brigadier)
    }
}