import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.gradle.jvm.tasks.Jar

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
    id("org.jetbrains.intellij.platform") version "2.9.0"
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

// Set the JVM language level used to build the project.
kotlin {
    jvmToolchain(21)
}

// Ensure ':core' project is evaluated so we can access its tasks/outputs
evaluationDependsOn(":core")

// Access ':core' runtime dependencies to package them (excluding core.jar)
val coreProjectRef = project(":core")
val coreRuntimeDepsProvider: Provider<Set<java.io.File>> = providers.provider {
    coreProjectRef.configurations.getByName("runtimeClasspath").resolve().filterNot { file ->
        file.name.startsWith("${coreProjectRef.name}-${coreProjectRef.version}")
    }.toSet()
}

// Configure project's dependencies
repositories {
    mavenCentral()

    // IntelliJ Platform Gradle Plugin Repositories Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-repositories-extension.html
    intellijPlatform {
        defaultRepositories()
    }
}

configurations.all {
    resolutionStrategy.sortArtifacts(ResolutionStrategy.SortOrder.DEFAULT)
}

dependencies {
    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        create(properties("platformType"), properties("platformVersion"))

        // Plugin Dependencies. Uses `platformBundledPlugins` property from the gradle.properties file for bundled IntelliJ Platform plugins.
        bundledPlugins(properties("platformBundledPlugins").map { it.split(',') })

        // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file for plugin from JetBrains Marketplace.
        plugins(properties("platformPlugins").map { it.split(',') })

        pluginVerifier()
        // testFramework(TestFrameworkType.Platform.JUnit4)
    }

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
    compileOnly(project(":core"))
    implementation(project(":launcher"))

    // Bring in all ':core' runtime dependencies so they are packaged with the plugin,
    // but exclude the core.jar itself because its classes are merged into this jar.
    runtimeOnly(files(coreRuntimeDepsProvider))
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    pluginConfiguration {
        version = properties("pluginVersion")
        description = file("src/main/html/description.html").inputStream().readBytes().toString(Charsets.UTF_8)
        changeNotes = file("src/main/html/change-notes.html").inputStream().readBytes().toString(Charsets.UTF_8)

        ideaVersion {
            sinceBuild = properties("pluginSinceBuild")
            untilBuild = properties("pluginUntilBuild")
        }
    }

    signing {
        certificateChain = environment("CERTIFICATE_CHAIN")
        privateKey = environment("PRIVATE_KEY")
        password = environment("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = environment("PUBLISH_TOKEN")
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels = properties("pluginVersion").map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }

    pluginVerification {
        ides {
            create(IntelliJPlatformType.IntellijIdeaUltimate, properties("platformVersion").get())
            recommended()
        }
    }

    buildSearchableOptions = false
}

// Ensure signing is not silently skipped when publishing
val hasSigningCreds = listOf("CERTIFICATE_CHAIN", "PRIVATE_KEY", "PRIVATE_KEY_PASSWORD")
    .all { environment(it).isPresent }

// Merge ':core' classes and resources into the main plugin JAR
val coreJarProvider = project(":core").tasks.named("jar")


tasks {
    // Merge core outputs into this module's jar to avoid a separate core.jar in the plugin lib
    named<Jar>("jar") {
        // Ensure the core jar is built first
        dependsOn(coreJarProvider)
        // Unpack the core jar contents into this jar
        from({ zipTree(coreJarProvider.get().outputs.files.singleFile) })
        // Avoid duplicate files when merging resources from ':core'
        duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE
    }
    // Fail early if trying to publish without signing credentials
    named("publishPlugin") {
        dependsOn("signPlugin")
        doFirst {
            if (!hasSigningCreds) {
                throw GradleException(
                    "Missing signing credentials. Set CERTIFICATE_CHAIN, PRIVATE_KEY, and PRIVATE_KEY_PASSWORD in the environment to sign before publishing."
                )
            }
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }
}
