plugins {
    kotlin("jvm")
}

group = "co.anbora.labs.jmeter.launcher"
version = "1.2.5"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin {
    jvmToolchain(17)
}
