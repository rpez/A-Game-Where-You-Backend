val ktorVersion: String by project
val kotlinVersion: String by project
val exposedVersion: String by project
val postgresVersion: String by project

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
}

group = "com.rekon"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(libs.logback.classic)

    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.serialization.kotlinx.json)

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
//    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion") // Optional
    implementation("org.postgresql:postgresql:${postgresVersion}")

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
