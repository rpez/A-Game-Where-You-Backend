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
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.serialization.kotlinx.json)

    implementation(libs.logback.classic)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.postgres)

    // Test environment
    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
    testImplementation(ktorLibs.client.contentNegotiation)
}
