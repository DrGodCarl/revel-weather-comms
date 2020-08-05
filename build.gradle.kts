plugins {
    java
    application
    kotlin("jvm") version "1.3.72"
}

group = "com.revelhealth"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "com.revelhealth.MainKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    // using an old version of retrofit because:
    //  - current version has warnings that spew out unless on java 14
    //  - I'm not using any features that 2.8.0+ offers
    implementation("com.squareup.retrofit2", "retrofit", "2.7.2")
    implementation("com.squareup.retrofit2", "converter-gson", "2.7.2")
    implementation("com.natpryce", "result4k", "2.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.mockk", "mockk", "1.10.0")
    testImplementation("io.kotest", "kotest-runner-junit5", "4.1.3")
    testImplementation("io.kotest", "kotest-property", "4.1.3")
}

tasks {
    test {
        useJUnitPlatform()
    }

    jar {
        manifest {
            attributes["Main-Class"] = "com.revelhealth.MainKt"
        }

        from(sourceSets.main.get().output)

        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()

    kotlinOptions {
        jvmTarget = "11"
    }

}
