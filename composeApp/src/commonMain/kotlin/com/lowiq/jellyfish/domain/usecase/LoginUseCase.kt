package com.lowiq.jellyfish.domain.usecase

import com.lowiq.jellyfish.domain.model.User
import com.lowiq.jellyfish.domain.repository.AuthRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val serverRepository: ServerRepository
) {
    suspend operator fun invoke(
        serverId: String,
        serverUrl: String,
        username: String,
        password: String
    ): Result<User> {
        return authRepository.login(serverUrl, username, password)
            .onSuccess {
                serverRepository.setActiveServer(serverId)
            }
    }
}
