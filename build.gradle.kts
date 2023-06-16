plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
    id("org.jetbrains.intellij") version "1.13.3"
}

group = "co.anbora.labs.jmeter"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // api means "the dependency is for both compilation and runtime"
    // runtime means "the dependency is only for runtime, not for compilation"
    // In other words, marking dependency as "runtime" would avoid accidental
    // dependency on it during compilation
    // Note: if there's at least single chance for the dependency to be needed on the
    // compilation classpath (e.g. it is used as a transitive by a third-party library)
    // then it should be declared as "api" here since we use useCompileClasspathVersions
    // to make runtime classpath consistent with the compile one.
    implementation("org.apache.jmeter:ApacheJMeter_java:5.4.3") {
        exclude("org.apache.jmeter", "bom")
    }
    implementation("org.apache.jmeter:ApacheJMeter_components:5.4.3") {
        exclude("org.apache.jmeter", "bom")
    }

    implementation("commons-io:commons-io:2.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.1")

    api("bsf:bsf:2.4.0")

    api("com.fifesoft:rsyntaxtextarea:3.3.3")

    api("com.github.weisj:darklaf-core:2.7.3")
    api("com.github.weisj:darklaf-theme:2.7.3")
    api("com.github.weisj:darklaf-extensions-rsyntaxarea:0.3.4")
    api("com.github.weisj:darklaf-property-loader:2.7.3")

    api("com.google.auto.service:auto-service-annotations:1.1.0")
    api("com.google.auto.service:auto-service:1.1.0")
    api("com.google.errorprone:error_prone_annotations:2.19.1")
    api("com.miglayout:miglayout-core:5.3")
    api("com.miglayout:miglayout-swing:5.3")
    api("com.sun.activation:javax.activation:1.2.0")
    api("com.thoughtworks.xstream:xstream:1.4.20")

    api("commons-lang:commons-lang:2.6")

    api("javax.activation:javax.activation-api:1.2.0")

    api("net.minidev:accessors-smart:2.4.11")
    api("net.minidev:json-smart:2.4.11")
    api("net.sf.jtidy:jtidy:r938")
    api("net.sf.saxon:Saxon-HE:11.5")

    api("org.apache.commons:commons-collections4:4.4")
    api("org.apache.commons:commons-dbcp2:2.9.0")
    api("org.apache.commons:commons-jexl3:3.2.1")
    api("org.apache.commons:commons-jexl:2.1.1")
    api("org.apache.commons:commons-lang3:3.12.0")
    api("org.apache.commons:commons-math3:3.6.1")
    api("org.apache.commons:commons-pool2:2.11.1")
    api("org.apache.commons:commons-text:1.10.0")
    api("org.apache.geronimo.specs:geronimo-jms_1.1_spec:1.1.1")

    api("org.apache.logging.log4j:log4j-core:2.20.0")

    api("org.apache.tika:tika-core:1.28.5")

    api("org.apiguardian:apiguardian-api:1.1.2")

    api("org.brotli:dec:0.1.2")
    api("org.exparity:hamcrest-date:2.0.8")
    api("org.freemarker:freemarker:2.3.32")

    api("org.jetbrains.lets-plot:lets-plot-batik:3.2.0")
    api("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.4.1")
    api("org.jetbrains:annotations:24.0.1")
    api("org.jodd:jodd-core:5.0.13")

    api("org.jodd:jodd-props:5.0.13")

    api("org.slf4j:jcl-over-slf4j:1.7.36")
    api("org.slf4j:slf4j-api:1.7.36")
    api("oro:oro:2.0.8")
    api("xalan:serializer:2.7.3")
    api("xalan:xalan:2.7.3")

    api("xml-apis:xml-apis:1.4.01")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.2.5")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
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
        sinceBuild.set("222")
        untilBuild.set("232.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
