plugins {
  id("org.hypertrace.repository-plugin") version "0.2.3"
  id("org.hypertrace.ci-utils-plugin") version "0.2.0"
  id("org.hypertrace.jacoco-report-plugin") version "0.1.3" apply false
  id("org.hypertrace.docker-java-application-plugin") version "0.8.0" apply false
  id("org.hypertrace.docker-publish-plugin") version "0.8.0" apply false
}

subprojects {
  group = "org.hypertrace.core.viewgenerator"
}
