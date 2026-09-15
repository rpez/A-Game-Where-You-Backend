package com.rekon.model

import kotlin.uuid.Uuid

interface TaskRepository {
    suspend fun allTasks(): List<Task>
    suspend fun tasksByType(type: TaskType): List<Task>
    suspend fun tasksByUserId(userId: String): List<Task>
    suspend fun taskById(id: Uuid): Task?
    suspend fun addTask(task: Task): Boolean
    suspend fun updateTask(task: Task): Boolean
    suspend fun deleteTask(id: Uuid): Boolean
}