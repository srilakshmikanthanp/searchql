plugins {
  kotlin("jvm") version "2.2.0"
  kotlin("plugin.noarg") version "2.2.0"
  kotlin("plugin.allopen") version "2.2.0"
}

group = "com.srilakshmikanthanp.searchql.jpa"
version = "1.0.0"

repositories {
  mavenCentral()
}

dependencies {
  implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
  implementation(project(":core"))

  testImplementation("org.hibernate.orm:hibernate-core:6.0.0.Final")
  testImplementation("com.h2database:h2:2.3.232")
  testImplementation(kotlin("test"))
}

kotlin {
  sourceSets {
    test {
      compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
      }
    }
  }
}

noArg {
  annotation("jakarta.persistence.Entity")
}

allOpen {
  annotation("jakarta.persistence.Embeddable")
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(11)
}
