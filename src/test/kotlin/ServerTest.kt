package com.rekon

import com.rekon.model.Task
import com.rekon.model.TaskRepository
import com.rekon.model.TaskType
import com.rekon.util.getNewUuid
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.testing.testApplication
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlin.test.*

suspend fun Application.configureTestApp() {
    dependencies {
        provide<TaskRepository> {
            FakeTaskRepository()
        }
    }
    configureSerialization()
    configureStatusPages()
    configureRouting()
}

class ServerTest {
    @Test
    fun `test root endpoint`() = testApplication {
        // loads default configuration
        configure()
        // verify server root returns 200
        assertEquals(HttpStatusCode.OK, client.get("/").status)
    }

    @Test
    fun tasksCanBeFoundByType() = testApplication {
        application {
            configureTestApp()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/tasks/byType/Everyone")
        val results = response.body<List<Task>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedTaskDescriptions = listOf("Do stuff", "Exist")
        val actualTaskDescriptions = results.map(Task::description)
        assertContentEquals(expectedTaskDescriptions, actualTaskDescriptions)
    }

    @Test
    fun invalidByTypeProduces400() = testApplication {
        application {
            configureTestApp()
        }
        val response = client.get("/tasks/byType/Invalid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun unusedByTypeProduces404() = testApplication {
        application {
            configureTestApp()
        }

        val response = client.get("/tasks/byType/Other")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun newTasksCanBeAdded() = testApplication {
        application {
            configureTestApp()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val task = Task(getNewUuid(), TaskType.SingleTarget, "Go to the beach", "user")
        val response1 = client.post("/tasks") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json
            )

            setBody(task)
        }
        assertEquals(HttpStatusCode.NoContent, response1.status)

        val response2 = client.get("/tasks")
        assertEquals(HttpStatusCode.OK, response2.status)

        val taskDescriptions = response2
            .body<List<Task>>()
            .map { it.description }

        assertContains(taskDescriptions, "Go to the beach")
    }
}
