package com.lowiq.jellyfish.presentation.screens.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.model.User
import com.lowiq.jellyfish.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val usernameError: String? = null,  // Inline validation
    val passwordError: String? = null   // Inline validation
)

sealed class LoginEvent {
    data class LoginSuccess(val user: User) : LoginEvent()
    data class NetworkError(val message: String) : LoginEvent()  // For Snackbar
}

class LoginScreenModel(
    private val server: Server,
    private val loginUseCase: LoginUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    val serverName: String = server.name

    fun updateUsername(username: String) {
        _state.update { it.copy(username = username, usernameError = null) }
    }

    fun updatePassword(password: String) {
        _state.update { it.copy(password = password, passwordError = null) }
    }

    fun login() {
        val username = _state.value.username.trim()
        val password = _state.value.password

        // Inline validation
        var hasError = false
        if (username.isBlank()) {
            _state.update { it.copy(usernameError = "Username is required") }
            hasError = true
        }
        if (password.isBlank()) {
            _state.update { it.copy(passwordError = "Password is required") }
            hasError = true
        }
        if (hasError) return

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            loginUseCase(server.id, server.url, username, password)
                .onSuccess { user ->
                    _state.update { it.copy(isLoading = false) }
                    _events.emit(LoginEvent.LoginSuccess(user))
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false) }
                    _events.emit(LoginEvent.NetworkError(e.message ?: "Login failed"))
                }
        }
    }
}
