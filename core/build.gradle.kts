plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij.platform.module")
}

group = "co.anbora.labs.jmeter.core"
version = "1.2.5"

repositories {
    mavenCentral()

    // IntelliJ Platform Gradle Plugin Repositories Extension
    intellijPlatform {
        defaultRepositories()
    }
}

configurations.all {
    resolutionStrategy.sortArtifacts(ResolutionStrategy.SortOrder.DEFAULT)
}

dependencies {
    // IntelliJ Platform Gradle Plugin Dependencies Extension
    intellijPlatform {
        // Create IntelliJ Platform dependency for this module to compile against the IDE APIs
        create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))
    }

    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib-jdk8
    // implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.0")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("org.ow2.asm:asm:9.6")

    implementation("commons-io:commons-io:2.12.0")
    implementation("bsf:bsf:2.4.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fifesoft:rsyntaxtextarea:3.3.4")
    implementation("com.github.ben-manes.caffeine:caffeine:2.9.3")
    implementation("com.github.weisj:darklaf-core:2.7.3")
    implementation("com.github.weisj:darklaf-theme:2.7.3")
    implementation("com.github.weisj:darklaf-extensions-rsyntaxarea:0.3.4")
    implementation("com.github.weisj:darklaf-property-loader:2.7.3")

    implementation("com.google.auto.service:auto-service-annotations:1.1.1")
    implementation("com.google.auto.service:auto-service:1.1.1")
    implementation("com.google.errorprone:error_prone_annotations:2.24.0")
    implementation("com.miglayout:miglayout-core:5.3")
    implementation("com.miglayout:miglayout-swing:5.3")
    implementation("com.sun.activation:javax.activation:1.2.0")
    implementation("com.thoughtworks.xstream:xstream:1.4.20")
    implementation("commons-codec:commons-codec:1.16.0")

    implementation("commons-lang:commons-lang:2.6")

    implementation("javax.activation:javax.activation-api:1.2.0")

    implementation("net.minidev:accessors-smart:2.5.0")
    implementation("net.minidev:json-smart:2.5.0")
    implementation("net.sf.jtidy:jtidy:r938")
    implementation("net.sf.saxon:Saxon-HE:11.6")

    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.apache.commons:commons-dbcp2:2.9.0")
    implementation("org.apache.commons:commons-jexl3:3.2.1")
    implementation("org.apache.commons:commons-jexl:2.1.1")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.apache.commons:commons-pool2:2.12.0")
    implementation("org.apache.commons:commons-text:1.11.0")
    implementation("org.apache.geronimo.specs:geronimo-jms_1.1_spec:1.1.1")

    implementation("org.apache.logging.log4j:log4j-core:2.22.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.22.1")

    implementation("org.apache.tika:tika-core:1.28.5")

    implementation("org.apiguardian:apiguardian-api:1.1.2")

    implementation("org.brotli:dec:0.1.2")
    implementation("org.exparity:hamcrest-date:2.0.8")
    implementation("org.freemarker:freemarker:2.3.32")

    implementation("org.jetbrains.lets-plot:lets-plot-batik:4.1.0")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.5.0")
    implementation("org.jetbrains:annotations:24.1.0")
    implementation("org.jodd:jodd-core:5.0.13")

    implementation("org.jodd:jodd-props:5.0.13")

    implementation("org.mozilla:rhino:1.7.14")

    implementation("org.slf4j:jcl-over-slf4j:1.7.36")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("oro:oro:2.0.8")
    implementation("xalan:serializer:2.7.3")
    implementation("xalan:xalan:2.7.3")
    implementation("xerces:xercesImpl:2.12.2")
    implementation("xml-apis:xml-apis:1.4.01")
    implementation("xmlpull:xmlpull:1.1.3.1")

    implementation("net.sf.json-lib:json-lib:2.4:jdk15")

    //implementation("org.apache.jmeter:ApacheJMeter_http:5.6.3")
    //implementation("org.apache.jmeter:ApacheJMeter_java:5.6.3")

    implementation(project(":jorphan"))
    implementation(project(":launcher"))
}

intellijPlatform {
    buildSearchableOptions = false
}

tasks {
    named("compileKotlin", org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask::class.java) {
        compilerOptions {
            freeCompilerArgs.add("-Xjvm-default=all")
        }
    }
}

kotlin {
    jvmToolchain(17)
}
