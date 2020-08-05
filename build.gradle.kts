plugins {
    java
    kotlin("jvm") version "1.3.72"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.ajalt", "clikt", "1.4.0")

    testImplementation("junit", "junit", "4.12")
}
