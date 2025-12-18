package com.lowiq.jellyfish.presentation.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.ActivityItem
import com.lowiq.jellyfish.domain.repository.AuthRepository
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeState(
    val username: String = "",
    val serverName: String = "",
    val activityFeed: List<ActivityItem> = emptyList(),
    val selectedNavIndex: Int = 0,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

sealed class HomeEvent {
    object LoggedOut : HomeEvent()
    object NavigateToServerList : HomeEvent()
}

class HomeScreenModel(
    private val serverRepository: ServerRepository,
    private val authRepository: AuthRepository,
    private val mediaRepository: MediaRepository
) : ScreenModel {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvent>()
    val events = _events.asSharedFlow()

    private var currentServerId: String? = null

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        screenModelScope.launch {
            serverRepository.getActiveServer()
                .filterNotNull()
                .first()
                .let { server ->
                    currentServerId = server.id
                    val user = authRepository.getCurrentUser(server.id)
                    _state.update {
                        it.copy(
                            username = user?.name ?: server.username ?: "User",
                            serverName = server.name,
                            isLoading = false
                        )
                    }
                    loadActivityFeed()
                }
        }
    }

    private fun loadActivityFeed() {
        val serverId = currentServerId ?: return
        screenModelScope.launch {
            _state.update { it.copy(error = null) }
            mediaRepository.getActivityFeed(serverId)
                .onSuccess { items ->
                    println("DEBUG: Loaded ${items.size} activity items")
                    _state.update { it.copy(activityFeed = items, isRefreshing = false) }
                }
                .onFailure { e ->
                    println("DEBUG: Failed to load activity feed: ${e.message}")
                    e.printStackTrace()
                    _state.update { it.copy(isRefreshing = false, error = e.message) }
                }
        }
    }

    fun refresh() {
        _state.update { it.copy(isRefreshing = true) }
        loadActivityFeed()
    }

    fun onNavigationItemSelected(index: Int) {
        _state.update { it.copy(selectedNavIndex = index) }
    }

    fun switchServer() {
        screenModelScope.launch {
            _events.emit(HomeEvent.NavigateToServerList)
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
