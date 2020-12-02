plugins {
  application
  jacoco
  id("org.hypertrace.docker-java-application-plugin")
  id("org.hypertrace.docker-publish-plugin")
  id("org.hypertrace.jacoco-report-plugin")
}

application {
  mainClassName = "org.hypertrace.core.viewcreator.ViewCreatorLauncher"
}

tasks.test {
  useJUnitPlatform()
}

dependencies {
  implementation(project(":hypertrace-core-view-generator-api"))
  implementation("org.hypertrace.core.viewcreator:view-creator-framework:0.1.19")

  testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
  testImplementation("org.mockito:mockito-core:3.6.28")
}

description = "view creator for Pinot"
