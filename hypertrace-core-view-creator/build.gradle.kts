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
  implementation("org.hypertrace.core.viewcreator:view-creator-framework:0.1.25")

  constraints {
    implementation("net.minidev:json-smart:2.4.1") {
      because("Denial of Service (DoS) [Medium Severity][https://snyk.io/vuln/SNYK-JAVA-NETMINIDEV-1078499] in net.minidev:json-smart@2.3")
    }
    implementation("org.yaml:snakeyaml:1.26") {
      because("Denial of Service (DoS) [Medium Severity][https://snyk.io/vuln/SNYK-JAVA-ORGYAML-537645] in org.yaml:snakeyaml@1.24")
    }
    implementation("org.apache.thrift:libthrift:0.14.0") {
      because("Denial of Service (DoS) [High Severity][https://snyk.io/vuln/SNYK-JAVA-ORGAPACHETHRIFT-1074898] in org.apache.thrift:libthrift@0.13.0")
    }
    implementation("org.apache.tomcat.embed:tomcat-embed-core:8.5.63") {
      because("Session Fixation [Low Severity][https://snyk.io/vuln/SNYK-JAVA-ORGAPACHETOMCATEMBED-538488] in org.apache.tomcat.embed:tomcat-embed-core@8.5.46")
      because("HTTP Request Smuggling [Medium Severity][https://snyk.io/vuln/SNYK-JAVA-ORGAPACHETOMCATEMBED-1080638] in org.apache.tomcat.embed:tomcat-embed-core@8.5.46")
      because("Information Exposure [Medium Severity][https://snyk.io/vuln/SNYK-JAVA-ORGAPACHETOMCATEMBED-1048292] in org.apache.tomcat.embed:tomcat-embed-core@8.5.46")
      because("Information Disclosure [Medium Severity][https://snyk.io/vuln/SNYK-JAVA-ORGAPACHETOMCATEMBED-1061939] in org.apache.tomcat.embed:tomcat-embed-core@8.5.46")
      because("Denial of Service (DoS) [Medium Severity][https://snyk.io/vuln/SNYK-JAVA-ORGAPACHETOMCATEMBED-584427] in org.apache.tomcat.embed:tomcat-embed-core@8.5.46")
      because("Remote Code Execution (RCE) [High Severity][https://snyk.io/vuln/SNYK-JAVA-ORGAPACHETOMCATEMBED-1080637] in org.apache.tomcat.embed:tomcat-embed-core@8.5.46")
      because("Remote Code Execution (RCE) [High Severity][https://snyk.io/vuln/SNYK-JAVA-ORGAPACHETOMCATEMBED-570072] in org.apache.tomcat.embed:tomcat-embed-core@8.5.46")
    }
  }

  testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
  testImplementation("org.mockito:mockito-core:3.6.28")
}

description = "view creator for Pinot"
