plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.17.1"
}

group = "co.anbora.labs.jmeter"
version = "1.4.1"

repositories {
    mavenCentral()
}

configurations.all {
    resolutionStrategy.sortArtifacts(ResolutionStrategy.SortOrder.DEFAULT)
}

dependencies {
    implementation("commons-io:commons-io:2.15.1")

    implementation("com.github.weisj:darklaf-core:2.7.3")
    implementation("com.github.weisj:darklaf-theme:2.7.3")
    implementation("com.github.weisj:darklaf-extensions-rsyntaxarea:0.3.4")
    implementation("com.github.weisj:darklaf-property-loader:2.7.3")

    implementation("org.apache.logging.log4j:log4j-1.2-api:2.22.1")
    implementation("org.apache.logging.log4j:log4j-api:2.22.1")
    implementation("org.apache.logging.log4j:log4j-core:2.22.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.22.1")

    implementation("com.google.auto.service:auto-service-annotations:1.1.1")
    implementation("com.google.auto.service:auto-service:1.1.1")

    implementation("net.sf.json-lib:json-lib:2.4:jdk15")

    implementation(project(":jorphan"))
    implementation(project(":core"))
    implementation(project(":launcher"))
}

apply {
    plugin("kotlin")
    plugin("org.jetbrains.intellij")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.2.2")
    type.set("IC") // Target IDE Platform
    downloadSources.set(true)
    plugins.set(listOf())
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("241.*")
        changeNotes.set(file("src/main/html/change-notes.html").inputStream().readBytes().toString(Charsets.UTF_8))
        pluginDescription.set(file("src/main/html/description.html").inputStream().readBytes().toString(Charsets.UTF_8))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    buildSearchableOptions {
        enabled = false
    }
}
