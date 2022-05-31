plugins {
  `java-library`
  id("com.github.davidmc24.gradle.plugin.avro") version "1.3.0"
}

sourceSets {
  main {
    java {
      srcDirs("src/main/java", "build/generated-main-avro-java")
    }
  }
}

dependencies {
  constraints {
    api("com.fasterxml.jackson.core:jackson-databind:2.12.6.1") {
      because("Denial of Service (DoS) [High Severity][https://snyk.io/vuln/SNYK-JAVA-COMFASTERXMLJACKSONCORE-2421244] in com.fasterxml.jackson.core:jackson-databind@2.12.5")
      because("introduced by org.apache.avro:avro@1.11.0 > com.fasterxml.jackson.core:jackson-databind@2.12.5")
    }
  }
  api("org.apache.avro:avro:1.11.0")
}

