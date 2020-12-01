rootProject.name = "hypertrace-core-view-generator"

pluginManagement {
  repositories {
    mavenLocal()
    gradlePluginPortal()
    maven("https://dl.bintray.com/hypertrace/maven")
  }
}

plugins {
  id("org.hypertrace.version-settings") version "0.1.5"
}

include(":hypertrace-core-view-generator-api")
include(":hypertrace-core-view-generator")
include(":hypertrace-core-view-creator")
