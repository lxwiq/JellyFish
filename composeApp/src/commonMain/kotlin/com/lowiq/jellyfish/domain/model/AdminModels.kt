package com.lowiq.jellyfish.domain.model

data class AdminUser(
    val id: String,
    val name: String,
    val isAdmin: Boolean,
    val isDisabled: Boolean
)

data class LogEntry(
    val timestamp: Long,
    val severity: String,
    val message: String
)

data class ScheduledTask(
    val id: String,
    val name: String,
    val description: String,
    val state: TaskState,
    val lastExecutionResult: String?
)

enum class TaskState {
    IDLE, RUNNING, CANCELLING
}
