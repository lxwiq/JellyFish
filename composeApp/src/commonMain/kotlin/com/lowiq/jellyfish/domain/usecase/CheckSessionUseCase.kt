package com.lowiq.jellyfish.domain.usecase

import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.model.User
import com.lowiq.jellyfish.domain.repository.AuthRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.first

class CheckSessionUseCase(
    private val authRepository: AuthRepository,
    private val serverRepository: ServerRepository
) {
    sealed class SessionState {
        data class Authenticated(val server: Server, val user: User) : SessionState()
        data class HasServers(val servers: List<Server>) : SessionState()
        object NoServers : SessionState()
    }

    suspend operator fun invoke(): SessionState {
        val servers = serverRepository.getServers().first()

        if (servers.isEmpty()) {
            return SessionState.NoServers
        }

        // Check active server first
        val activeServer = serverRepository.getActiveServer().first()
        if (activeServer != null && authRepository.isAuthenticated(activeServer.id)) {
            val user = authRepository.getCurrentUser(activeServer.id)
            if (user != null) {
                return SessionState.Authenticated(activeServer, user)
            }
        }

        // Check other servers for valid session
        for (server in servers) {
            if (authRepository.isAuthenticated(server.id)) {
                val user = authRepository.getCurrentUser(server.id)
                if (user != null) {
                    serverRepository.setActiveServer(server.id)
                    return SessionState.Authenticated(server, user)
                }
            }
        }

        return SessionState.HasServers(servers)
    }
}
