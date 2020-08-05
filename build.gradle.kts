plugins {
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
    implementation("com.squareup.retrofit2", "retrofit", "2.9.0")
    implementation("com.squareup.retrofit2", "converter-gson", "2.9.0")
    testImplementation("junit", "junit", "4.12")
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.revelhealth.MainKt"
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
