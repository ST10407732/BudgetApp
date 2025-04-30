// settings.gradle.kts

pluginManagement {
    repositories {
        google() // Ensure google repository is available for plugin resolution
        mavenCentral() // Ensure Maven Central is available for plugin resolution
        gradlePluginPortal() // Ensure Gradle Plugin Portal is included for plugin resolution
        maven("https://jitpack.io") // 👉 Add this
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // Ensure google repository is available for resolving all dependencies
        mavenCentral() // Ensure mavenCentral is available for resolving dependencies
        maven("https://jitpack.io") // 👉 Add this
    }
}

rootProject.name = "BudgetTracker"
include(":app")
