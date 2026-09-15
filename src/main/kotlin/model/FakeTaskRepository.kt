package com.rekon.model

import org.slf4j.LoggerFactory

class FakeTaskRepository : TaskRepository {
    private val logger = LoggerFactory.getLogger(TaskRepository::class.java)

    private val tasks = mutableListOf(
        Task(TaskType.Everyone, "Task1", "Do stuff", "1", "user"),
        Task(TaskType.Modifier, "Task2", "Be weird", "2", "user"),
        Task(TaskType.SingleTarget, "Task3", "What is this", "3", "user"),
        Task(TaskType.Everyone, "Task4", "Exist", "4", "user")
    )

    override fun allTasks(): List<Task> = tasks

    override fun tasksByType(type: TaskType): List<Task> = tasks.filter {
        it.type == type
    }

    override fun tasksByUserId(userId: String): List<Task> = tasks.filter {
        it.creator == userId
    }

    override fun taskById(id: String): Task? = tasks.find {
        it.id == id
    }

    override fun addTask(task: Task): Boolean {
        if (taskById(task.id) != null) {
            logger.warn("Task (id: {}) add failed: can't add duplicate tasks.", task.id)
            return false
        }
        tasks.add(task)
        return true
    }

    override fun updateTask(task: Task): Boolean {
        if (taskById(task.id) == null) {
            logger.warn("Task (id: {}) update failed: task not found.", task.id)
            return false
        }
        deleteTask(task.id)
        addTask(task)
        return true
    }

    override fun deleteTask(id: String): Boolean {
        if (taskById(id) == null) {
            logger.warn("Task (id: {}) delete failed: task not found.", id)
            return false
        }
        tasks.removeIf { it.id == id }
        return true
    }
}