package com.rekon

import io.ktor.server.application.*
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun Application.configureExposed() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/agamewhereyou",
        user = "devuser",
        password = "devpass"
    )
}