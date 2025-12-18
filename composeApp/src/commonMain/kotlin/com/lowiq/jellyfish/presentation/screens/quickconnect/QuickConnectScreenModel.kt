package com.lowiq.jellyfish.presentation.screens.quickconnect

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.model.User
import com.lowiq.jellyfish.domain.usecase.QuickConnectUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class QuickConnectState(
    val code: String? = null,
    val isInitializing: Boolean = true,
    val isPolling: Boolean = false,
    val error: String? = null
)

sealed class QuickConnectEvent {
    data class AuthSuccess(val user: User) : QuickConnectEvent()
    data class NetworkError(val message: String) : QuickConnectEvent()
}

class QuickConnectScreenModel(
    private val server: Server,
    private val quickConnectUseCase: QuickConnectUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(QuickConnectState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<QuickConnectEvent>()
    val events = _events.asSharedFlow()

    private var quickConnectJob: Job? = null

    val serverName: String = server.name

    init {
        startQuickConnect()
    }

    private fun startQuickConnect() {
        quickConnectJob?.cancel()
        quickConnectJob = screenModelScope.launch {
            _state.update { it.copy(isInitializing = true, error = null, code = null) }

            quickConnectUseCase.invoke(server.id, server.url)
                .collect { useCaseState ->
                    when (useCaseState) {
                        is QuickConnectUseCase.State.ShowCode -> {
                            _state.update {
                                it.copy(
                                    code = useCaseState.code,
                                    isInitializing = false,
                                    isPolling = true
                                )
                            }
                        }
                        is QuickConnectUseCase.State.Polling -> {
                            _state.update { it.copy(isPolling = true) }
                        }
                        is QuickConnectUseCase.State.Success -> {
                            _state.update { it.copy(isPolling = false) }
                            _events.emit(QuickConnectEvent.AuthSuccess(useCaseState.user))
                        }
                        is QuickConnectUseCase.State.Error -> {
                            _state.update {
                                it.copy(
                                    isInitializing = false,
                                    isPolling = false,
                                    error = useCaseState.message
                                )
                            }
                            _events.emit(QuickConnectEvent.NetworkError(useCaseState.message))
                        }
                    }
                }
        }
    }

    fun retry() {
        startQuickConnect()
    }

    fun cancel() {
        quickConnectJob?.cancel()
    }

    override fun onDispose() {
        quickConnectJob?.cancel()
        super.onDispose()
    }
}
