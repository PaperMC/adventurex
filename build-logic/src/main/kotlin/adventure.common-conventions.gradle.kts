import com.adarshr.gradle.testlogger.theme.ThemeType
import com.diffplug.gradle.spotless.FormatExtension
import net.ltgt.gradle.errorprone.errorprone

plugins {
  id("adventure.base-conventions")
  id("net.kyori.indra")
  id("net.kyori.indra.checkstyle")
  id("net.kyori.indra.licenser.spotless")
  id("net.kyori.indra.publishing")
  id("com.adarshr.test-logger")
  id("com.diffplug.eclipse.apt")
  id("net.ltgt.errorprone")
  jacoco
}
// expose version catalog
val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

testlogger {
  theme = ThemeType.MOCHA_PARALLEL
  showPassed = false
}

configurations {
  testCompileClasspath {
    exclude(group = "junit") // brought in by google's libs
  }
}

dependencies {
  errorprone(libs.errorprone)
  annotationProcessor(libs.contractValidator)
  api(platform(libs.adventure.bom))
  checkstyle(libs.stylecheck)
  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.api)
  testImplementation(libs.junit.engine)
  testImplementation(libs.junit.params)
  testRuntimeOnly(libs.junit.launcher)
}

spotless {
  fun FormatExtension.applyCommon() {
    trimTrailingWhitespace()
    endWithNewline()
    leadingTabsToSpaces(2)
  }
  java {
    importOrderFile(rootProject.file(".spotless/kyori.importorder"))
    removeUnusedImports()
    applyCommon()
  }
  kotlinGradle {
    applyCommon()
  }
}

val ADVENTURE_PREFIX = "adventurex-"

tasks {
  javadoc {
    val options = options as? StandardJavadocDocletOptions ?: return@javadoc
    options.applyCommonJavadocOptions()
  }

  jacocoTestReport {
    dependsOn(test)
  }

  withType(JavaCompile::class).configureEach {
    options.errorprone {
      disable("InvalidBlockTag") // we use custom block tags
      disable("InlineMeSuggester") // we don't use errorprone annotations
      disable("ReferenceEquality") // lots of comparison against EMPTY objects
      disable("CanIgnoreReturnValueSuggester") // suggests errorprone annotation, not JB Contract annotation
    }

    options.compilerArgs.add("-Xlint:all")
    options.compilerArgs.add("-Xlint:-processing") // unclaimed ap warnings are not needed
    options.compilerArgs.add("-Xlint:-serial") // nobody cares about serialization
  }
}

tasks.register("shouldDoRelease") {
  description = "Checks if this module should be released."
  doLast {
    if (project.version.toString().contains("SNAPSHOT")) {
      return@doLast
    }
    logger.lifecycle("do release ${project.projectDir.name}")
  }
}

indra {
  signWithKeyFromPrefixedProperties("papermc")
}
