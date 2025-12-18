package com.lowiq.jellyfish.domain.usecase

import com.lowiq.jellyfish.domain.model.User
import com.lowiq.jellyfish.domain.repository.AuthRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QuickConnectUseCase(
    private val authRepository: AuthRepository,
    private val serverRepository: ServerRepository
) {
    sealed class State {
        data class ShowCode(val code: String) : State()
        object Polling : State()
        data class Success(val user: User) : State()
        data class Error(val message: String) : State()
    }

    fun invoke(serverId: String, serverUrl: String): Flow<State> = flow {
        // Initiate Quick Connect
        val codeResult = authRepository.initiateQuickConnect(serverUrl)

        if (codeResult.isFailure) {
            emit(State.Error(codeResult.exceptionOrNull()?.message ?: "Failed to initiate Quick Connect"))
            return@flow
        }

        val code = codeResult.getOrThrow()
        emit(State.ShowCode(code))

        // Poll for authentication (secret is the code in this case)
        var attempts = 0
        val maxAttempts = 60 // 5 minutes with 5 second intervals

        while (attempts < maxAttempts) {
            emit(State.Polling)
            delay(5000) // Poll every 5 seconds

            val pollResult = authRepository.pollQuickConnect(serverUrl, code)

            if (pollResult.isFailure) {
                val error = pollResult.exceptionOrNull()
                if (error?.message?.contains("expired", ignoreCase = true) == true) {
                    emit(State.Error("Quick Connect session expired"))
                    return@flow
                }
                // Continue polling on other errors
                attempts++
                continue
            }

            val user = pollResult.getOrNull()
            if (user != null) {
                serverRepository.setActiveServer(serverId)
                emit(State.Success(user))
                return@flow
            }

            attempts++
        }

        emit(State.Error("Quick Connect session timed out"))
    }
}
