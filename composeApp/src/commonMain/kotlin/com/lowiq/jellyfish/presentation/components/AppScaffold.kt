package com.lowiq.jellyfish.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val SidebarWidth = 56.dp

@Composable
fun AppScaffold(
    selectedIndex: Int,
    onNavigationItemSelected: (Int) -> Unit,
    showSidebar: Boolean,
    activeDownloadCount: Int = 0,
    downloadProgress: Float = 0f,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val sidebarOffset by animateDpAsState(
        targetValue = if (showSidebar) 0.dp else -SidebarWidth,
        animationSpec = tween(durationMillis = 250)
    )

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .width(SidebarWidth + sidebarOffset)
                .fillMaxHeight()
        ) {
            NavigationRail(
                selectedIndex = selectedIndex,
                onItemSelected = onNavigationItemSelected,
                activeDownloadCount = activeDownloadCount,
                downloadProgress = downloadProgress,
                modifier = Modifier
                    .fillMaxHeight()
                    .offset(x = sidebarOffset)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            content()
        }
    }
}
