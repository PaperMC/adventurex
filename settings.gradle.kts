import org.gradle.kotlin.dsl.provider.inLenientMode

pluginManagement {
  includeBuild("build-logic")
  repositories {
    maven(url = "https://repo.stellardrift.ca/maven/internal/") {
      name = "stellardriftReleases"
      mavenContent { releasesOnly() }
    }
    maven(url = "https://repo.papermc.io/repository/maven-snapshots/") {
      name = "papermcSnapshots"
      mavenContent { snapshotsOnly() }
    }
    maven(url = "https://repo.stellardrift.ca/maven/snapshots/") {
      name = "stellardriftSnapshots"
      mavenContent { snapshotsOnly() }
    }
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven(url = "https://libraries.minecraft.net") {
      name = "minecraft"
      mavenContent {
        includeGroup("com.mojang")
        releasesOnly()
      }
    }
  }
}

rootProject.name = "adventurex-parent"

sequenceOf(
  "nbt-dfu"
).forEach {
  include("adventurex-$it")
  project(":adventurex-$it").projectDir = file(it)
}
