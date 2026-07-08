plugins {
  id("adventure.common-conventions")
}

version = "0.1.1-SNAPSHOT"

dependencies {
  api(libs.adventure.nbt)
  api(libs.dfu)
}

applyJarMetadata("io.papermc.adventurex.nbt.dfu")
