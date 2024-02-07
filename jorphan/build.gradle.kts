plugins {
    kotlin("jvm")
}

group = "co.anbora.labs.jmeter.jorphan"
version = "1.2.5"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib-jdk8
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.0")

    implementation("org.apiguardian:apiguardian-api:1.1.2")
    implementation("org.slf4j:slf4j-api:1.7.36")

    implementation("commons-io:commons-io:2.12.0")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.apache.commons:commons-text:1.10.0")
}

kotlin {
    jvmToolchain(17)
}
