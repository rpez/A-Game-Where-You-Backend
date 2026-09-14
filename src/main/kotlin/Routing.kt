package com.rekon

import io.ktor.http.HttpHeaders
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.cors.routing.*
    
import io.ktor.server.plugins.statuspages.StatusPages

fun Application.configureRouting() {
    val environment: String = (System.getenv("ENVIRONMENT") ?: "development").trim()
    println("[$environment]")
    if (environment == "development") {
        println(environment)
        install(CORS)
        {
            val host: String? = System.getenv("FRONTEND_DEV")?.trim()
            if (host != null)
            {
                println("[$host]")
                allowHost(host, listOf("http"))
                allowHeader(HttpHeaders.ContentType)
            }
        }
    }
    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respondText("App in illegal state: ${cause.message}")
        }
    }
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/error-test") {
            throw IllegalStateException("Test Error")
        }
        get("health") {
            call.respondText("Ok")
        }
    }
}