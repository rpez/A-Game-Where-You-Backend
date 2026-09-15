package com.rekon

import com.rekon.model.Task
import com.rekon.model.TaskRepository
import com.rekon.model.TaskType
import org.slf4j.LoggerFactory
import com.rekon.util.getNewUuid
import kotlin.uuid.Uuid

class FakeTaskRepository : TaskRepository {
    private val logger = LoggerFactory.getLogger(TaskRepository::class.java)

    private val tasks = mutableListOf(
        Task(getNewUuid(), TaskType.Everyone, "Do stuff", "user"),
        Task(getNewUuid(), TaskType.Modifier, "Be weird", "user"),
        Task(getNewUuid(), TaskType.SingleTarget, "What is this", "user"),
        Task(getNewUuid(), TaskType.Everyone, "Exist", "user")
    )

    override suspend fun allTasks(): List<Task> = tasks

    override suspend fun tasksByType(type: TaskType): List<Task> = tasks.filter {
        it.type == type
    }

    override suspend fun tasksByUserId(userId: String): List<Task> = tasks.filter {
        it.creator == userId
    }

    override suspend fun taskById(id: Uuid): Task? = tasks.find {
        it.id == id
    }

    override suspend fun addTask(task: Task): Boolean {
        if (taskById(task.id) != null) {
            logger.warn("Task (id: {}) add failed: can't add duplicate tasks.", task.id)
            return false
        }
        tasks.add(task)
        return true
    }

    override suspend fun updateTask(task: Task): Boolean {
        if (taskById(task.id) == null) {
            logger.warn("Task (id: {}) update failed: task not found.", task.id)
            return false
        }
        deleteTask(task.id)
        addTask(task)
        return true
    }

    override suspend fun deleteTask(id: Uuid): Boolean {
        if (taskById(id) == null) {
            logger.warn("Task (id: {}) delete failed: task not found.", id)
            return false
        }
        tasks.removeIf { it.id == id }
        return true
    }
}