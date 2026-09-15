package com.rekon.model

import kotlinx.serialization.Serializable

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
data class Task(val type: TaskType,
                val displayName: String = "",
                val description: String = "",
                val id: String = "",
                val creator: String = "",
)