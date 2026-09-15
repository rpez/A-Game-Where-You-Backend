package com.rekon.model

interface TaskRepository {
    fun allTasks(): List<Task>
    fun tasksByType(type: TaskType): List<Task>
    fun tasksByUserId(userId: String): List<Task>
    fun taskById(id: String): Task?
    fun addTask(task: Task): Boolean
    fun updateTask(task: Task): Boolean
    fun deleteTask(id: String): Boolean
}