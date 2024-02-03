plugins {
    kotlin("jvm")
}

group = "org.example"
version = "1.2.5"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib-jdk8
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.0")

    implementation("bsf:bsf:2.4.0")
    implementation("org.apiguardian:apiguardian-api:1.1.2")
    implementation("com.fifesoft:rsyntaxtextarea:3.3.4")

    implementation("commons-io:commons-io:2.15.1")

    implementation("org.apache.logging.log4j:log4j-1.2-api:2.22.1")
    implementation("org.apache.logging.log4j:log4j-api:2.22.1")
    implementation("org.apache.logging.log4j:log4j-core:2.22.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.22.1")

    implementation("com.google.auto.service:auto-service-annotations:1.1.1")
    implementation("com.google.auto.service:auto-service:1.1.1")

    implementation("org.apache.commons:commons-lang3:3.14.0")

    implementation(project(":core"))
    implementation(project(":jorphan"))
}

kotlin {
    jvmToolchain(17)
}