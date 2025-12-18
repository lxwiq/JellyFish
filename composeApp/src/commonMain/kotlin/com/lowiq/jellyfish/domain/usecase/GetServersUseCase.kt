package com.lowiq.jellyfish.domain.usecase

import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow

class GetServersUseCase(
    private val serverRepository: ServerRepository
) {
    operator fun invoke(): Flow<List<Server>> = serverRepository.getServers()
}
