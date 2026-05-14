plugins {
  id("adventure.common-conventions")
}

version = "1.0.0-SNAPSHOT"

dependencies {
  api(libs.adventure.nbt)
  api(libs.dfu)
}

applyJarMetadata("io.papermc.adventurex.nbt.dfu")
