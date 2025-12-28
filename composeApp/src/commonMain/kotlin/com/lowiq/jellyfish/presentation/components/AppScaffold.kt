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
import com.lowiq.jellyfish.domain.model.Library
import com.lowiq.jellyfish.isDesktopPlatform

private val CollapsedSidebarWidth = 56.dp
private val ExpandedSidebarWidth = 200.dp

@Composable
fun AppScaffold(
    selectedIndex: Int,
    onNavigationItemSelected: (Int) -> Unit,
    showSidebar: Boolean,
    activeDownloadCount: Int = 0,
    downloadProgress: Float = 0f,
    libraries: List<Library> = emptyList(),
    onLibraryClick: ((Library) -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isDesktop = isDesktopPlatform()
    val sidebarWidth = if (isDesktop) ExpandedSidebarWidth else CollapsedSidebarWidth

    val sidebarOffset by animateDpAsState(
        targetValue = if (isDesktop || showSidebar) 0.dp else -sidebarWidth,
        animationSpec = tween(durationMillis = 250)
    )

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .width(sidebarWidth + sidebarOffset)
                .fillMaxHeight()
        ) {
            NavigationRail(
                selectedIndex = selectedIndex,
                onItemSelected = onNavigationItemSelected,
                activeDownloadCount = activeDownloadCount,
                downloadProgress = downloadProgress,
                expanded = isDesktop,
                libraries = libraries,
                onLibraryClick = onLibraryClick,
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
