package com.rekon.model

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

enum class TaskType {
    SingleTarget,
    MultiTarget,
    Opponents,
    Everyone,
    Modifier,
    Permanent,
    Special,
    Other
}

@Serializable
data class CreateTaskRequest(val type: TaskType,
                             val description: String
)

@Serializable
data class Task(val id: Uuid,
                val type: TaskType,
                val description: String,
                val creator: String,
)