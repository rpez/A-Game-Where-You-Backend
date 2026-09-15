package com.rekon.model

import com.rekon.db.TaskTable
import com.rekon.db.toTask
import com.rekon.db.withTransaction
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertIgnore
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

class PostgresTaskRepository : TaskRepository {
    override suspend fun allTasks(): List<Task> = withTransaction {
        TaskTable.selectAll()
            .map { it.toTask() }
    }

    override suspend fun tasksByType(type: TaskType): List<Task> = withTransaction {
        TaskTable.selectAll()
            .where { TaskTable.type eq type }
            .map { it.toTask() }
    }

    override suspend fun tasksByUserId(userId: String): List<Task> = withTransaction {
        // TODO: use UUID for users
        TaskTable.selectAll()
            .where { TaskTable.creator eq userId }
            .map { it.toTask() }
    }

    override suspend fun taskById(id: Uuid): Task? = withTransaction {
        TaskTable.selectAll()
            .where { TaskTable.id eq id.toJavaUuid() }
            .map { it.toTask() }
            .firstOrNull()
    }

    override suspend fun addTask(task: Task): Boolean = withTransaction {
        TaskTable.insertIgnore {
            it[id] = task.id.toJavaUuid()
            it[type] = task.type
            it[description] = task.description
            it[creator] = task.creator
        }.insertedCount > 0
    }

    override suspend fun updateTask(task: Task): Boolean = withTransaction {
        TaskTable.update({ TaskTable.id eq task.id.toJavaUuid() }) {
            it[type] = task.type
            it[description] = task.description
            it[creator] = task.creator
        } > 0
    }

    override suspend fun deleteTask(id: Uuid): Boolean = withTransaction {
        TaskTable.deleteWhere { TaskTable.id eq id.toJavaUuid() } > 0
    }

}