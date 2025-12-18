package com.lowiq.jellyfish.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.presentation.components.ActivityFeedItem
import com.lowiq.jellyfish.presentation.components.AppHeader
import com.lowiq.jellyfish.presentation.components.AppScaffold
import com.lowiq.jellyfish.presentation.screens.serverlist.ServerListScreen

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<HomeScreenModel>()
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is HomeEvent.LoggedOut -> {
                        navigator.replaceAll(ServerListScreen())
                    }
                    is HomeEvent.NavigateToServerList -> {
                        navigator.push(ServerListScreen())
                    }
                }
            }
        }

        AppScaffold(
            selectedIndex = state.selectedNavIndex,
            onNavigationItemSelected = { screenModel.onNavigationItemSelected(it) },
            modifier = Modifier.background(Color(0xFF09090B))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AppHeader(
                    username = state.username,
                    onSwitchServer = { screenModel.switchServer() },
                    onLogout = { screenModel.logout() }
                )

                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFFAFAFA))
                    }
                } else {
                    ActivityFeed(
                        state = state,
                        onItemClick = { /* TODO: Navigate to detail */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityFeed(
    state: HomeState,
    onItemClick: (String) -> Unit
) {
    if (state.error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Error: ${state.error}",
                color = Color(0xFFFF6B6B)
            )
        }
    } else if (state.activityFeed.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No activity yet",
                color = Color(0xFFA1A1AA)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09090B)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = state.activityFeed,
                key = { it.id }
            ) { item ->
                ActivityFeedItem(
                    title = item.title,
                    subtitle = item.subtitle,
                    timestamp = item.timestamp,
                    imageUrl = item.imageUrl,
                    progress = item.progress,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}
