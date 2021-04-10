rootProject.name = "hypertrace-core-view-generator"

pluginManagement {
  repositories {
    mavenLocal()
    gradlePluginPortal()
    maven("https://hypertrace.jfrog.io/artifactory/maven")
  }
}

plugins {
  id("org.hypertrace.version-settings") version "0.2.0"
}

include(":hypertrace-core-view-generator-api")
include(":hypertrace-core-view-generator")
include(":hypertrace-core-view-creator")
