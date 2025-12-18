package com.lowiq.jellyfish.presentation.screens.addserver

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.usecase.AddServerUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AddServerState(
    val url: String = "",
    val isLoading: Boolean = false,
    val error: String? = null  // Inline error for validation
)

sealed class AddServerEvent {
    data class ServerAdded(val server: Server) : AddServerEvent()
    data class NetworkError(val message: String) : AddServerEvent()  // For Snackbar
}

class AddServerScreenModel(
    private val addServerUseCase: AddServerUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(AddServerState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<AddServerEvent>()
    val events = _events.asSharedFlow()

    fun updateUrl(url: String) {
        _state.update { it.copy(url = url, error = null) }
    }

    fun submitServer() {
        val url = _state.value.url.trim()

        // Inline validation
        if (url.isBlank()) {
            _state.update { it.copy(error = "Server URL cannot be empty") }
            return
        }

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            addServerUseCase(url)
                .onSuccess { server ->
                    _state.update { it.copy(isLoading = false) }
                    _events.emit(AddServerEvent.ServerAdded(server))
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false) }
                    _events.emit(AddServerEvent.NetworkError(e.message ?: "Failed to connect to server"))
                }
        }
    }
}
