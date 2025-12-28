package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun AppHeader(
    username: String,
    isSidebarOpen: Boolean,
    onMenuClick: () -> Unit,
    onSwitchServer: () -> Unit,
    onLogout: () -> Unit,
    showMenuButton: Boolean = true,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current
    var menuExpanded by remember { mutableStateOf(false) }

    val iconRotation by animateFloatAsState(
        targetValue = if (isSidebarOpen) 90f else 0f,
        animationSpec = tween(durationMillis = 250)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(colors.background)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Burger menu (if shown) + App name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = if (showMenuButton) 0.dp else 8.dp)
        ) {
            if (showMenuButton) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = if (isSidebarOpen) Icons.Default.Close else Icons.Default.Menu,
                        contentDescription = if (isSidebarOpen) "Close menu" else "Open menu",
                        tint = colors.foreground,
                        modifier = Modifier.rotate(iconRotation)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = "JellyFish",
                color = colors.foreground,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
        }

        // Right side: User avatar with dropdown menu
        Box {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(colors.secondary)
                    .clickable { menuExpanded = true },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = username.firstOrNull()?.uppercase() ?: "?",
                    color = colors.foreground,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Switch Server") },
                    onClick = {
                        menuExpanded = false
                        onSwitchServer()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Logout") },
                    onClick = {
                        menuExpanded = false
                        onLogout()
                    }
                )
            }
        }
    }
}
