package com.rekon

import io.ktor.http.HttpHeaders
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.cors.routing.*
    
import io.ktor.server.plugins.statuspages.StatusPages

fun Application.configureRouting() {
    // Configure environment
    val environment: String = (System.getenv("ENVIRONMENT") ?: "development").trim()
    if (environment == "development") {
        install(CORS)
        {
            val host: String? = System.getenv("FRONTEND_DEV")?.trim()
            if (host != null)
            {
                allowHost(host, listOf("http"))
                allowHeader(HttpHeaders.ContentType)
            }
        }
    }

    // Configure routing
    routing {
        get("/") {
            call.respondText("Welcome to a Game Where You _____!")
        }
        // Static plugin for testing at `/static/index.html`
        staticResources("/static", "static")
    }
}