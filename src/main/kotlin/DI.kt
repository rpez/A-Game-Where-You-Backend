package com.rekon

import com.rekon.model.FakeTaskRepository
import com.rekon.model.TaskRepository
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies

fun Application.configureDependencyInjection() {
    dependencies {
        provide<TaskRepository> {
            FakeTaskRepository()
        }
    }
}