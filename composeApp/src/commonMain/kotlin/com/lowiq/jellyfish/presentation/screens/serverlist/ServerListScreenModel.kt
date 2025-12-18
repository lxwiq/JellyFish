package com.lowiq.jellyfish.presentation.screens.serverlist

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.usecase.GetServersUseCase
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ServerListState(
    val servers: List<Server> = emptyList(),
    val isLoading: Boolean = true
)

class ServerListScreenModel(
    private val getServersUseCase: GetServersUseCase,
    private val serverRepository: ServerRepository
) : ScreenModel {

    private val _state = MutableStateFlow(ServerListState())
    val state = _state.asStateFlow()

    init {
        loadServers()
    }

    private fun loadServers() {
        screenModelScope.launch {
            getServersUseCase()
                .onStart { _state.update { it.copy(isLoading = true) } }
                .collect { servers ->
                    _state.update { it.copy(servers = servers, isLoading = false) }
                }
        }
    }

    fun selectServer(serverId: String) {
        screenModelScope.launch {
            serverRepository.setActiveServer(serverId)
        }
    }

    fun deleteServer(serverId: String) {
        screenModelScope.launch {
            serverRepository.removeServer(serverId)
        }
    }
}
