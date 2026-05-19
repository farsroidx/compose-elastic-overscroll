pluginManagement {

    repositories {

        mavenLocal()

        val isJitpackBuild =
            System.getenv("JITPACK") == "true" ||
                System.getenv("CI") == "true"

        if (!isJitpackBuild) {
            maven(url = "https://maven.myket.ir")
        }

        gradlePluginPortal()

        mavenCentral()

        google()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

val isJitpackBuild =
    System.getenv("JITPACK") == "true" ||
        System.getenv("CI") == "true"

logger.log(LogLevel.INFO, "isJitpackBuild: $isJitpackBuild")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        mavenLocal()
        if (!isJitpackBuild) {
            maven(url = "https://maven.myket.ir")
        }
        mavenCentral()
        google()
        maven(url = "https://jitpack.io")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ComposeElasticOverscroll"

if (!isJitpackBuild) { include(":app") }

include(":overscroll")