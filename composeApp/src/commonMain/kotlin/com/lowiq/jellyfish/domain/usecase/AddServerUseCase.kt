package com.lowiq.jellyfish.domain.usecase

import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.repository.ServerRepository

class AddServerUseCase(
    private val serverRepository: ServerRepository
) {
    suspend operator fun invoke(url: String): Result<Server> {
        if (url.isBlank()) {
            return Result.failure(IllegalArgumentException("Server URL cannot be empty"))
        }
        return serverRepository.addServer(url)
    }
}
