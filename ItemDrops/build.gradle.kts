import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.orangishcat"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Provided by the host game; needed for compilation only
    compileOnly(files("../lib/Tanks-1.6.e-014cfef3.jar"))
    // Explicit for clarity; stdlib will be shaded into the fat JAR
    implementation(kotlin("stdlib"))
}

tasks.withType<ShadowJar>().configureEach {
    // Shade only Kotlin runtime bits; keep external deps out
    dependencies {
        include(dependency("org.jetbrains.kotlin:.*"))
    }
    archiveClassifier.set("")
}

tasks.named("jar") {
    enabled = false
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}
