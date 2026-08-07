plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "2.1.1"
    id("com.gradleup.shadow") version "9.2.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.codechicken:DiffPatch:2.1.0.43:all") {
        isTransitive = false
    }
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.7.0.202606012155-r") {
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
            "io.codechicken",
            "com.googlecode",
            "org.apache",
            "org.eclipse",
        ).forEach {
            relocate(it, "${project.group}.eyepatch.libs.$it")
        }
    }
}
