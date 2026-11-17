pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("androidx.*")
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google {
      content {
        includeGroupByRegex("androidx.*")
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
      }
    }
    mavenCentral()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// The Android CLI installs the SDK without setting ANDROID_HOME, so a fresh clone can't find it.
// Ask the CLI where it is and remember the answer in local.properties.
val localProperties = file("local.properties")
if (!localProperties.exists() && System.getenv("ANDROID_HOME").isNullOrBlank()) {
  runCatching { providers.exec { commandLine("android", "info", "sdk") }.standardOutput.asText.get().trim() }
    .getOrNull()
    ?.takeIf { it.isNotEmpty() }
    ?.let { sdk -> localProperties.writeText("sdk.dir=${sdk.replace('\\', '/')}\n") }
}

rootProject.name = "Kisumu Market"

include(":app")
