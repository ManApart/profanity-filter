import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
}

group = "me.austin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://github.com/psiegman/mvn-repo/raw/master/releases")
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("nl.siegmann.epublib:epublib-core:3.1") {
        exclude("xmlpull")
    }
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}