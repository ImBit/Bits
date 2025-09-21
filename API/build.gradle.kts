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
        maven {
            url = uri("https://repo.biomebattle.net/repository/biomebattle-repo/")
            credentials {
                username = env.BB_REPO_USERNAME.value
                password = env.BB_REPO_PASSWORD.value
            }
        }
    }
}