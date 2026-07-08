plugins {
  id("adventure.common-conventions")
}

version = "0.1.2"

dependencies {
  api(libs.adventure.nbt)
  api(libs.dfu)
}

applyJarMetadata("io.papermc.adventurex.nbt.dfu")
