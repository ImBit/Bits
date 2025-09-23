description = "🦑 Utility API for Bits Plugin development."

repositories {
}

dependencies {
}

tasks.withType<Jar> {
    archiveBaseName.set("Bits-API")
}

/**
 * Publishing is configured to use the {@link https://github.com/BiomeBattle/} repository for now,
 * this is the main place we use it.
 * Public repo access will be implemented in the future, when we see a need for it! :)
 */
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "bits"
        }
    }
    repositories {
        val repoUser = System.getenv("BB_REPO_USERNAME")
        val repoPass = System.getenv("BB_REPO_PASSWORD")
        if (repoUser != null && repoPass != null) {
            maven {
                url = uri("https://repo.biomebattle.net/repository/biomebattle-repo/")
                credentials {
                    username = repoUser
                    password = repoPass
                }
            }
        }
    }
}