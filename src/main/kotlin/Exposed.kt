package com.rekon

import io.ktor.server.application.*
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun Application.configureExposed() {
    Database.connect(
        url = environment.config.property("ktor.database.url").getString(),
        user = environment.config.property("ktor.database.user").getString(),
        password = environment.config.property("ktor.database.password").getString()
    )
}