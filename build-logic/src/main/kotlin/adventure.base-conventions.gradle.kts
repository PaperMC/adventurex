plugins {
  id("net.kyori.indra.publishing")
}

// expose version catalog
val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

indra {
  javaVersions {
    target(21)
    val testVersions = (project.property("testJdks") as String)
      .split(",")
      .map { it.trim().toInt() }
    testWith().addAll(testVersions)
  }
  checkstyle(libs.versions.checkstyle.get())

  github("PaperMC", "adventurex") {
    ci(true)
  }
  mitLicense()

  signWithKeyFromPrefixedProperties("papermc")
  configurePublications {
    pom {
      developers {
        developer {
          id = "kezz"
        }
      }
    }
  }
}
