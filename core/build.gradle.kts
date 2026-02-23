plugins {
  kotlin("jvm") version "2.2.0"
  antlr
}

group = "com.srilakshmikanthanp.searchql.core"
version = "1.0.0"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.antlr:antlr4-runtime:4.13.2")
  testImplementation(kotlin("test"))
  antlr("org.antlr:antlr4:4.13.2")
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(17)
}

tasks.generateGrammarSource {
  arguments = arguments + listOf("-long-messages", "-no-listener", "-visitor", "-package", "com.srilakshmikanthanp.searchql.core.grammar")
  outputDirectory = file("${layout.buildDirectory.get()}/generated-src/antlr/main/java/com/srilakshmikanthanp/searchql/core/grammar")
}

tasks.compileKotlin {
  dependsOn(tasks.generateGrammarSource)
}

sourceSets.main {
  java.srcDir("${layout.buildDirectory.get()}/generated-src/antlr/main/java")
}
