import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.maven.publishing)
    alias(libs.plugins.detekt)
}

group = "io.github.faiyaz26"
version = "0.0.4"

val detektVersion = libs.versions.detekt.get()

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("compiler-embeddable"))
    runtimeOnly(kotlin("reflect"))

    implementation(libs.detekt.api)

    runtimeOnly(libs.detekt.formatting)

    testImplementation(libs.assertj)
    testImplementation(libs.detekt.test)
    testImplementation(libs.guava)
    testImplementation(libs.junit.jupiter.api)

    testRuntimeOnly(libs.junit.jupiter.engine)

    detektPlugins(libs.detekt.formatting)
    detektPlugins(libs.detekt.ruleauthors)
    detektPlugins(libs.detekt.compiler.wrapper)
    detektPlugins(rootProject)
}

detekt {
    parallel = true
    autoCorrect = true

    buildUponDefaultConfig = true
    config.from(rootProject.file("detekt.yaml"))

    allRules = false // activate all available (even unstable) rules.
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

mavenPublishing {
    coordinates("io.github.faiyaz26", "inclusive-language-detekt-rules", version as String)

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    pom {
        name = "inclusive-language-detekt-rules"
        description = "Detekt rules to have inclusive language in code"
        inceptionYear = "2024"
        url = "https://github.com/faiyaz26/inclusive-language-detekt-rules"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                distribution = "repo"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        developers {
            developer {
                id = "faiyaz26"
                name = "Ahmad Faiyaz"
            }
        }

        issueManagement {
            system = "GitHub"
            url = "https://github.com/faiyaz26/inclusive-language-detekt-rules/issues"
        }

        scm {
            connection = "scm:git:git://github.com/faiyaz26/inclusive-language-detekt-rules.git"
            url = "https://github.com/faiyaz26/inclusive-language-detekt-rules"
        }
    }
}