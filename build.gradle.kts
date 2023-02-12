plugins {
    kotlin("multiplatform") version "1.8.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-netty:2.1.1")
                implementation("io.ktor:ktor-server-html-builder-jvm:2.1.1")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
                implementation("io.ktor:ktor-server-call-logging-jvm:2.1.1")
                implementation("io.ktor:ktor-server-content-negotiation:2.1.1")
                implementation("io.ktor:ktor-serialization-jackson:2.1.1")

                implementation("ch.qos.logback:logback-classic:1.4.5")
                implementation("org.slf4j:slf4j-api:2.0.6")

                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
                implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.1")

            }
        }
        val jvmTest by getting{
            dependencies {
                implementation("io.ktor:ktor-server-test-host:2.1.1")
                implementation("org.jetbrains.kotlin:kotlin-test:1.7.21")
            }

        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.385")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.385")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.10.4-pre.385")
            }
        }
        val jsTest by getting
    }
}

application {
    mainClass.set("org.example.application.ServerKt")
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}