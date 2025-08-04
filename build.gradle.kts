plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.3.0"
    id("com.gradleup.shadow") version "8.3.5"
}

repositories {
    mavenCentral()
    maven("https://maven.covers1624.net/")
}

dependencies {
    implementation("codechicken:DiffPatch:1.5.0.30:all") {
        isTransitive = false
    }
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.3.0.202506031305-r") {
        exclude("org.slf4j")
    }
}

kotlin {
    jvmToolchain(8)
}

gradlePlugin {
    website = "https://github.com/GliczDev/EyePatch"
    vcsUrl = "https://github.com/GliczDev/EyePatch"

    plugins {
        create("eyepatch") {
            id = "$group.eyepatch"
            implementationClass = "$group.eyepatch.EyePatchPlugin"

            displayName = "eyepatch"
            description = "An (eye) patch to your repositories! "
            tags = listOf("git", "patch", "patches")
        }
    }
}

tasks {
    shadowJar {
        archiveClassifier = null

        listOf(
            "codechicken",
            "com.googlecode",
            "org.apache",
            "org.eclipse",
        ).forEach {
            relocate(it, "${project.group}.eyepatch.libs.$it")
        }
    }
}
