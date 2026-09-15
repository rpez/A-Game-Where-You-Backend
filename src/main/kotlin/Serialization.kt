package com.rekon

import com.rekon.model.CreateTaskRequest
import com.rekon.model.TaskType
import com.rekon.model.Task
import com.rekon.model.TaskRepository
import com.rekon.util.getNewUuid
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID
import kotlin.uuid.Uuid

suspend fun Application.configureSerialization() {
    val repository = dependencies.resolve<TaskRepository>()

    install(ContentNegotiation) {
        json()
    }
    routing {
        route("/tasks") {
            get {
                val tasks = repository.allTasks()
                println(tasks)
                call.respond(tasks)
            }

            get("/byId/{id}") {
                val id: String? = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val uuid: Uuid = try {
                    Uuid.parse(id)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val task = repository.taskById(uuid)
                if (task == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(task)
            }

            get("/byType/{type}") {
                val typeAsText: String? = call.parameters["type"]
                if (typeAsText == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                try {
                    val type = TaskType.valueOf(typeAsText)
                    val tasks = repository.tasksByType(type)

                    if (tasks.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(tasks)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post {
                try {
                    val taskRequest: CreateTaskRequest = call.receive<CreateTaskRequest>()
                    repository.addTask(
                        Task(getNewUuid(),
                            taskRequest.type,
                            taskRequest.description,
                            "test-user"
                        )
                    )
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            delete("/{id}") {
                val id: String? = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                val uuid: Uuid = try {
                    Uuid.parse(id)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (repository.deleteTask(uuid)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}