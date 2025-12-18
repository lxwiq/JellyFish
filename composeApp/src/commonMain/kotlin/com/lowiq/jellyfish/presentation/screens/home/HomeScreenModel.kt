package com.lowiq.jellyfish.presentation.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.repository.AuthRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeState(
    val username: String = "",
    val serverName: String = "",
    val isLoading: Boolean = true
)

sealed class HomeEvent {
    object LoggedOut : HomeEvent()
}

class HomeScreenModel(
    private val serverRepository: ServerRepository,
    private val authRepository: AuthRepository
) : ScreenModel {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvent>()
    val events = _events.asSharedFlow()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        screenModelScope.launch {
            serverRepository.getActiveServer()
                .filterNotNull()
                .first()
                .let { server ->
                    val user = authRepository.getCurrentUser(server.id)
                    _state.update {
                        it.copy(
                            username = user?.name ?: server.username ?: "User",
                            serverName = server.name,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun logout() {
        screenModelScope.launch {
            serverRepository.getActiveServer()
                .filterNotNull()
                .first()
                .let { server ->
                    authRepository.logout(server.id)
                    _events.emit(HomeEvent.LoggedOut)
                }
        }
    }
}
