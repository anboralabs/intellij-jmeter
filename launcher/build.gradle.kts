plugins {
    kotlin("jvm")
}

group = "org.example"
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