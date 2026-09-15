package com.rekon.db

import com.rekon.model.Task
import com.rekon.model.TaskType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.inTopLevelSuspendTransaction
import kotlin.uuid.toKotlinUuid

object TaskTable : UUIDTable("task") {
    val type = enumerationByName("type", 50, TaskType::class)
    val description = varchar("description", 50)
    val creator = varchar("creator", 50)
}

fun ResultRow.toTask() = Task(
    id = this[TaskTable.id].value.toKotlinUuid(),
    type = this[TaskTable.type],
    description = this[TaskTable.description],
    creator = this[TaskTable.creator]
)

suspend fun <T> withTransaction(block: suspend JdbcTransaction.() -> T): T = withContext(Dispatchers.IO) {
    inTopLevelSuspendTransaction { block() }
}