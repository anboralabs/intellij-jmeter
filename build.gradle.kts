plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.16.1"
}

group = "co.anbora.labs.jmeter"
version = "1.2.2"

repositories {
    mavenCentral()
}

configurations.all {
    resolutionStrategy.sortArtifacts(ResolutionStrategy.SortOrder.DEFAULT)
}

dependencies {
    implementation("commons-io:commons-io:2.12.0")
    implementation("bsf:bsf:2.4.0")

    implementation("com.fifesoft:rsyntaxtextarea:3.3.3")

    implementation("com.github.weisj:darklaf-core:2.7.3")
    implementation("com.github.weisj:darklaf-theme:2.7.3")
    implementation("com.github.weisj:darklaf-extensions-rsyntaxarea:0.3.4")
    implementation("com.github.weisj:darklaf-property-loader:2.7.3")

    implementation("com.google.auto.service:auto-service-annotations:1.1.0")
    implementation("com.google.auto.service:auto-service:1.1.0")
    implementation("com.google.errorprone:error_prone_annotations:2.19.1")
    implementation("com.miglayout:miglayout-core:5.3")
    implementation("com.miglayout:miglayout-swing:5.3")
    implementation("com.sun.activation:javax.activation:1.2.0")
    implementation("com.thoughtworks.xstream:xstream:1.4.20")

    implementation("commons-lang:commons-lang:2.6")

    implementation("javax.activation:javax.activation-api:1.2.0")

    implementation("net.minidev:accessors-smart:2.4.11")
    implementation("net.minidev:json-smart:2.4.11")
    implementation("net.sf.jtidy:jtidy:r938")
    implementation("net.sf.saxon:Saxon-HE:11.5")

    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.apache.commons:commons-dbcp2:2.9.0")
    implementation("org.apache.commons:commons-jexl3:3.2.1")
    implementation("org.apache.commons:commons-jexl:2.1.1")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.apache.commons:commons-pool2:2.11.1")
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("org.apache.geronimo.specs:geronimo-jms_1.1_spec:1.1.1")

    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")

    implementation("org.apache.tika:tika-core:1.28.5")

    implementation("org.apiguardian:apiguardian-api:1.1.2")

    implementation("org.brotli:dec:0.1.2")
    implementation("org.exparity:hamcrest-date:2.0.8")
    implementation("org.freemarker:freemarker:2.3.32")

    implementation("org.jetbrains.lets-plot:lets-plot-batik:3.2.0")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.4.1")
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("org.jodd:jodd-core:5.0.13")

    implementation("org.jodd:jodd-props:5.0.13")

    implementation("org.slf4j:jcl-over-slf4j:1.7.36")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("oro:oro:2.0.8")
    implementation("xalan:serializer:2.7.3")
    implementation("xalan:xalan:2.7.3")
    implementation("xerces:xercesImpl:2.12.2")
    implementation("xml-apis:xml-apis:1.4.01")
    implementation("xmlpull:xmlpull:1.1.3.1")

    implementation("net.sf.json-lib:json-lib:2.4:jdk15")

    implementation(fileTree(mapOf("dir" to "jmeter-libs", "include" to listOf("*.jar"))))
}

apply {
    plugin("kotlin")
    plugin("org.jetbrains.intellij")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("LATEST-EAP-SNAPSHOT")
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
        untilBuild.set("233.*")
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
