package com.lowiq.jellyfish.domain.repository

import com.lowiq.jellyfish.domain.model.AdminUser
import com.lowiq.jellyfish.domain.model.LogEntry
import com.lowiq.jellyfish.domain.model.ScheduledTask

interface AdminRepository {
    suspend fun getUsers(): Result<List<AdminUser>>
    suspend fun createUser(username: String, password: String): Result<AdminUser>
    suspend fun deleteUser(userId: String): Result<Unit>
    suspend fun refreshLibrary(): Result<Unit>
    suspend fun getServerLogs(limit: Int = 100): Result<List<LogEntry>>
    suspend fun getScheduledTasks(): Result<List<ScheduledTask>>
    suspend fun runTask(taskId: String): Result<Unit>
}
