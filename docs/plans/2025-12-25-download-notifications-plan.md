# Download Progress Indicator & Notifications Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Add real-time download progress visibility in the sidebar (circular progress + badge) and Android background notifications with grouped style.

**Architecture:** Create a `DownloadStateHolder` that aggregates download events into observable state. The sidebar's `NavigationRail` will display a `DownloadIndicator` composable. Android notifications use `NotificationManager` with a foreground-aware lifecycle via `ProcessLifecycleOwner`.

**Tech Stack:** Kotlin Multiplatform, Compose Multiplatform, Koin DI, Android NotificationCompat, ProcessLifecycleOwner

---

## Task 1: Create DownloadStateHolder

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadStateHolder.kt`

**Step 1: Create the DownloadStateHolder class**

```kotlin
package com.lowiq.jellyfish.domain.download

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ActiveDownload(
    val id: String,
    val title: String,
    val progress: Float
)

class DownloadStateHolder(
    private val downloadManager: DownloadManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _activeDownloads = MutableStateFlow<List<ActiveDownload>>(emptyList())
    val activeDownloads: StateFlow<List<ActiveDownload>> = _activeDownloads.asStateFlow()

    private val _activeCount = MutableStateFlow(0)
    val activeCount: StateFlow<Int> = _activeCount.asStateFlow()

    private val _averageProgress = MutableStateFlow(0f)
    val averageProgress: StateFlow<Float> = _averageProgress.asStateFlow()

    init {
        observeDownloadEvents()
    }

    private fun observeDownloadEvents() {
        scope.launch {
            downloadManager.downloadEvents.collect { event ->
                when (event) {
                    is DownloadEvent.Started -> {
                        _activeDownloads.update { list ->
                            list + ActiveDownload(event.downloadId, event.title, 0f)
                        }
                        updateDerivedState()
                    }
                    is DownloadEvent.Progress -> {
                        _activeDownloads.update { list ->
                            list.map { download ->
                                if (download.id == event.downloadId) {
                                    download.copy(progress = event.progress)
                                } else download
                            }
                        }
                        updateDerivedState()
                    }
                    is DownloadEvent.Completed, is DownloadEvent.Failed -> {
                        val downloadId = when (event) {
                            is DownloadEvent.Completed -> event.downloadId
                            is DownloadEvent.Failed -> event.downloadId
                            else -> return@collect
                        }
                        _activeDownloads.update { list ->
                            list.filter { it.id != downloadId }
                        }
                        updateDerivedState()
                    }
                }
            }
        }
    }

    private fun updateDerivedState() {
        val downloads = _activeDownloads.value
        _activeCount.value = downloads.size
        _averageProgress.value = if (downloads.isEmpty()) 0f
            else downloads.map { it.progress }.average().toFloat()
    }
}
```

**Step 2: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadStateHolder.kt
git commit -m "feat(download): add DownloadStateHolder for aggregating download state"
```

---

## Task 2: Create DownloadIndicator Composable

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/DownloadIndicator.kt`

**Step 1: Create the DownloadIndicator composable**

```kotlin
package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun DownloadIndicator(
    activeCount: Int,
    averageProgress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    strokeWidth: Dp = 3.dp
) {
    if (activeCount == 0) return

    val colors = LocalJellyFishColors.current
    val progressColor = colors.primary
    val trackColor = colors.muted
    val badgeColor = colors.destructive

    Box(modifier = modifier.size(size)) {
        // Circular progress arc
        Canvas(modifier = Modifier.size(size)) {
            val stroke = strokeWidth.toPx()
            val diameter = size.toPx() - stroke
            val topLeft = Offset(stroke / 2, stroke / 2)

            // Track (background circle)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )

            // Progress arc
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * averageProgress,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        // Badge with count
        Box(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.TopEnd)
                .offset(x = 2.dp, y = (-2).dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(16.dp)) {
                drawCircle(color = badgeColor)
            }
            Text(
                text = if (activeCount > 9) "9+" else activeCount.toString(),
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
```

**Step 2: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/DownloadIndicator.kt
git commit -m "feat(ui): add DownloadIndicator composable with circular progress and badge"
```

---

## Task 3: Update NavigationRail to Display Download Indicator

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/NavigationRail.kt`

**Step 1: Update NavigationRail signature and add download indicator**

Replace the entire file content:

```kotlin
package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

data class NavigationItem(
    val icon: ImageVector,
    val contentDescription: String
)

private val navigationItems = listOf(
    NavigationItem(Icons.Default.Home, "Home"),
    NavigationItem(Icons.Default.Search, "Search"),
    NavigationItem(Icons.Default.Star, "Favorites"),
    NavigationItem(Icons.Default.Download, "Downloads"),
    NavigationItem(Icons.Default.Settings, "Settings")
)

private const val DOWNLOADS_INDEX = 3

@Composable
fun NavigationRail(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    activeDownloadCount: Int = 0,
    downloadProgress: Float = 0f,
    modifier: Modifier = Modifier
) {
    val shapes = JellyFishTheme.shapes
    Column(
        modifier = modifier
            .width(56.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        navigationItems.forEachIndexed { index, item ->
            if (index == DOWNLOADS_INDEX && activeDownloadCount > 0) {
                DownloadNavigationItem(
                    icon = item.icon,
                    contentDescription = item.contentDescription,
                    isSelected = index == selectedIndex,
                    onClick = { onItemSelected(index) },
                    shapes = shapes,
                    activeCount = activeDownloadCount,
                    progress = downloadProgress
                )
            } else {
                NavigationRailItem(
                    icon = item.icon,
                    contentDescription = item.contentDescription,
                    isSelected = index == selectedIndex,
                    onClick = { onItemSelected(index) },
                    shapes = shapes
                )
            }
        }
    }
}

@Composable
private fun NavigationRailItem(
    icon: ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    shapes: com.lowiq.jellyfish.presentation.theme.JellyFishShapes,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Box(
        modifier = modifier
            .size(56.dp)
            .clip(shapes.default)
            .background(
                if (isSelected) colors.secondary else Color.Transparent
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isSelected) colors.foreground else colors.mutedForeground,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun DownloadNavigationItem(
    icon: ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    shapes: com.lowiq.jellyfish.presentation.theme.JellyFishShapes,
    activeCount: Int,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Box(
        modifier = modifier
            .size(56.dp)
            .clip(shapes.default)
            .background(
                if (isSelected) colors.secondary else Color.Transparent
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            DownloadIndicator(
                activeCount = activeCount,
                averageProgress = progress,
                size = 40.dp,
                strokeWidth = 3.dp
            )
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (isSelected) colors.foreground else colors.mutedForeground,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
```

**Step 2: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/NavigationRail.kt
git commit -m "feat(ui): integrate DownloadIndicator into NavigationRail"
```

---

## Task 4: Update AppScaffold to Pass Download State

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/AppScaffold.kt`

**Step 1: Update AppScaffold signature**

Replace the entire file content:

```kotlin
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
```

**Step 2: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/AppScaffold.kt
git commit -m "feat(ui): pass download state through AppScaffold to NavigationRail"
```

---

## Task 5: Register DownloadStateHolder in Koin

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt`

**Step 1: Add DownloadStateHolder to dataModule**

Add import at the top:
```kotlin
import com.lowiq.jellyfish.domain.download.DownloadStateHolder
```

Add to `dataModule` after the `DownloadManager` line:
```kotlin
    single { DownloadStateHolder(get()) }
```

**Step 2: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt
git commit -m "feat(di): register DownloadStateHolder in Koin"
```

---

## Task 6: Update HomeScreen to Use DownloadStateHolder

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreen.kt`

**Step 1: Add imports and inject DownloadStateHolder**

Add imports:
```kotlin
import com.lowiq.jellyfish.domain.download.DownloadStateHolder
import org.koin.compose.koinInject
```

**Step 2: Collect download state in Content()**

Inside the `Content()` function, after `var sidebarVisible by remember { mutableStateOf(false) }`, add:
```kotlin
        val downloadStateHolder = koinInject<DownloadStateHolder>()
        val activeDownloadCount by downloadStateHolder.activeCount.collectAsState()
        val downloadProgress by downloadStateHolder.averageProgress.collectAsState()
```

**Step 3: Pass to AppScaffold**

Update the AppScaffold call:
```kotlin
        AppScaffold(
            selectedIndex = state.selectedNavIndex,
            onNavigationItemSelected = {
                screenModel.onNavigationItemSelected(it)
                sidebarVisible = false
            },
            showSidebar = sidebarVisible,
            activeDownloadCount = activeDownloadCount,
            downloadProgress = downloadProgress,
            modifier = Modifier.background(colors.background)
        ) {
```

**Step 4: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreen.kt
git commit -m "feat(home): connect DownloadStateHolder to sidebar indicator"
```

---

## Task 7: Create DownloadNotifier expect/actual

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadNotifier.kt`
- Create: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadNotifier.android.kt`
- Create: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadNotifier.jvm.kt`
- Create: `composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadNotifier.ios.kt`

**Step 1: Create expect class in commonMain**

```kotlin
package com.lowiq.jellyfish.domain.download

expect class DownloadNotifier {
    fun updateProgress(downloadId: String, title: String, progress: Float)
    fun showCompleted(downloadId: String, title: String)
    fun showFailed(downloadId: String, title: String, error: String)
    fun cancel(downloadId: String)
    fun cancelAll()
    fun setForegroundEnabled(enabled: Boolean)
}
```

**Step 2: Create actual class for Android**

```kotlin
package com.lowiq.jellyfish.domain.download

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lowiq.jellyfish.MainActivity
import kotlinx.coroutines.*

actual class DownloadNotifier(
    private val context: Context
) {
    private val notificationManager = NotificationManagerCompat.from(context)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var foregroundEnabled = false
    private val activeNotifications = mutableSetOf<String>()

    companion object {
        private const val CHANNEL_ID = "downloads"
        private const val CHANNEL_NAME = "Téléchargements"
        private const val GROUP_KEY = "com.lowiq.jellyfish.DOWNLOADS"
        private const val SUMMARY_ID = 0
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Progression des téléchargements"
                setShowBadge(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    actual fun setForegroundEnabled(enabled: Boolean) {
        foregroundEnabled = enabled
        if (!enabled) {
            cancelAll()
        }
    }

    actual fun updateProgress(downloadId: String, title: String, progress: Float) {
        if (!foregroundEnabled) return

        val notificationId = downloadId.hashCode()
        activeNotifications.add(downloadId)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(title)
            .setContentText("${(progress * 100).toInt()}%")
            .setProgress(100, (progress * 100).toInt(), false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setGroup(GROUP_KEY)
            .setContentIntent(pendingIntent)
            .build()

        try {
            notificationManager.notify(notificationId, notification)
            updateSummaryNotification()
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }

    actual fun showCompleted(downloadId: String, title: String) {
        if (!foregroundEnabled) return

        val notificationId = downloadId.hashCode()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle(title)
            .setContentText("✓ Terminé")
            .setAutoCancel(true)
            .setGroup(GROUP_KEY)
            .build()

        try {
            notificationManager.notify(notificationId, notification)
        } catch (e: SecurityException) {
            // Permission not granted
        }

        // Remove after 3 seconds
        scope.launch {
            delay(3000)
            cancel(downloadId)
        }
    }

    actual fun showFailed(downloadId: String, title: String, error: String) {
        if (!foregroundEnabled) return

        val notificationId = downloadId.hashCode()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle(title)
            .setContentText("Échec: $error")
            .setAutoCancel(true)
            .setGroup(GROUP_KEY)
            .build()

        try {
            notificationManager.notify(notificationId, notification)
        } catch (e: SecurityException) {
            // Permission not granted
        }

        activeNotifications.remove(downloadId)
        updateSummaryNotification()
    }

    actual fun cancel(downloadId: String) {
        notificationManager.cancel(downloadId.hashCode())
        activeNotifications.remove(downloadId)
        updateSummaryNotification()
    }

    actual fun cancelAll() {
        activeNotifications.forEach { downloadId ->
            notificationManager.cancel(downloadId.hashCode())
        }
        activeNotifications.clear()
        notificationManager.cancel(SUMMARY_ID)
    }

    private fun updateSummaryNotification() {
        if (activeNotifications.isEmpty()) {
            notificationManager.cancel(SUMMARY_ID)
            return
        }

        val summaryNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("JellyFish")
            .setContentText("${activeNotifications.size} téléchargement(s) en cours")
            .setGroup(GROUP_KEY)
            .setGroupSummary(true)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()

        try {
            notificationManager.notify(SUMMARY_ID, summaryNotification)
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }
}
```

**Step 3: Create no-op actual for JVM**

```kotlin
package com.lowiq.jellyfish.domain.download

actual class DownloadNotifier {
    actual fun updateProgress(downloadId: String, title: String, progress: Float) {}
    actual fun showCompleted(downloadId: String, title: String) {}
    actual fun showFailed(downloadId: String, title: String, error: String) {}
    actual fun cancel(downloadId: String) {}
    actual fun cancelAll() {}
    actual fun setForegroundEnabled(enabled: Boolean) {}
}
```

**Step 4: Create no-op actual for iOS**

```kotlin
package com.lowiq.jellyfish.domain.download

actual class DownloadNotifier {
    actual fun updateProgress(downloadId: String, title: String, progress: Float) {}
    actual fun showCompleted(downloadId: String, title: String) {}
    actual fun showFailed(downloadId: String, title: String, error: String) {}
    actual fun cancel(downloadId: String) {}
    actual fun cancelAll() {}
    actual fun setForegroundEnabled(enabled: Boolean) {}
}
```

**Step 5: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinJvm :composeApp:compileKotlinAndroid`
Expected: BUILD SUCCESSFUL

**Step 6: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadNotifier.kt
git add composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadNotifier.android.kt
git add composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadNotifier.jvm.kt
git add composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadNotifier.ios.kt
git commit -m "feat(notifications): add DownloadNotifier with expect/actual for all platforms"
```

---

## Task 8: Register DownloadNotifier in Platform Modules

**Files:**
- Modify: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.android.kt`
- Modify: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.jvm.kt`
- Modify: `composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.ios.kt`

**Step 1: Add to Android platform module**

Add import:
```kotlin
import com.lowiq.jellyfish.domain.download.DownloadNotifier
```

Add to module:
```kotlin
    single { DownloadNotifier(androidContext()) }
```

**Step 2: Add to JVM platform module**

Add import:
```kotlin
import com.lowiq.jellyfish.domain.download.DownloadNotifier
```

Add to module:
```kotlin
    single { DownloadNotifier() }
```

**Step 3: Add to iOS platform module**

Add import:
```kotlin
import com.lowiq.jellyfish.domain.download.DownloadNotifier
```

Add to module:
```kotlin
    single { DownloadNotifier() }
```

**Step 4: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinJvm :composeApp:compileKotlinAndroid`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.android.kt
git add composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.jvm.kt
git add composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.ios.kt
git commit -m "feat(di): register DownloadNotifier in all platform modules"
```

---

## Task 9: Create AppLifecycleObserver for Android

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/download/AppLifecycleObserver.kt`
- Create: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/domain/download/AppLifecycleObserver.android.kt`
- Create: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/domain/download/AppLifecycleObserver.jvm.kt`
- Create: `composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/domain/download/AppLifecycleObserver.ios.kt`

**Step 1: Create expect class in commonMain**

```kotlin
package com.lowiq.jellyfish.domain.download

expect class AppLifecycleObserver(
    downloadManager: DownloadManager,
    downloadNotifier: DownloadNotifier
) {
    fun start()
}
```

**Step 2: Create actual for Android**

```kotlin
package com.lowiq.jellyfish.domain.download

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

actual class AppLifecycleObserver actual constructor(
    private val downloadManager: DownloadManager,
    private val downloadNotifier: DownloadNotifier
) : DefaultLifecycleObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var isObservingEvents = false

    actual fun start() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        // App came to foreground
        downloadNotifier.setForegroundEnabled(false)
        isObservingEvents = false
    }

    override fun onStop(owner: LifecycleOwner) {
        // App went to background
        downloadNotifier.setForegroundEnabled(true)
        if (!isObservingEvents) {
            isObservingEvents = true
            observeDownloadEvents()
        }
    }

    private fun observeDownloadEvents() {
        scope.launch {
            downloadManager.downloadEvents.collect { event ->
                when (event) {
                    is DownloadEvent.Started -> {
                        downloadNotifier.updateProgress(event.downloadId, event.title, 0f)
                    }
                    is DownloadEvent.Progress -> {
                        // We need title but Progress event doesn't have it
                        // For now, use downloadId as title placeholder
                        downloadNotifier.updateProgress(event.downloadId, event.downloadId, event.progress)
                    }
                    is DownloadEvent.Completed -> {
                        downloadNotifier.showCompleted(event.downloadId, event.title)
                    }
                    is DownloadEvent.Failed -> {
                        downloadNotifier.showFailed(event.downloadId, event.title, event.error)
                    }
                }
            }
        }
    }
}
```

**Step 3: Create no-op actual for JVM**

```kotlin
package com.lowiq.jellyfish.domain.download

actual class AppLifecycleObserver actual constructor(
    private val downloadManager: DownloadManager,
    private val downloadNotifier: DownloadNotifier
) {
    actual fun start() {}
}
```

**Step 4: Create no-op actual for iOS**

```kotlin
package com.lowiq.jellyfish.domain.download

actual class AppLifecycleObserver actual constructor(
    private val downloadManager: DownloadManager,
    private val downloadNotifier: DownloadNotifier
) {
    actual fun start() {}
}
```

**Step 5: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinJvm :composeApp:compileKotlinAndroid`
Expected: BUILD SUCCESSFUL

**Step 6: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/download/AppLifecycleObserver.kt
git add composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/domain/download/AppLifecycleObserver.android.kt
git add composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/domain/download/AppLifecycleObserver.jvm.kt
git add composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/domain/download/AppLifecycleObserver.ios.kt
git commit -m "feat(lifecycle): add AppLifecycleObserver for foreground/background detection"
```

---

## Task 10: Update DownloadStateHolder to Include Title in Progress Events

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadStateHolder.kt`

**Step 1: Add notifier integration**

Update constructor and add notification handling:

```kotlin
package com.lowiq.jellyfish.domain.download

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ActiveDownload(
    val id: String,
    val title: String,
    val progress: Float
)

class DownloadStateHolder(
    private val downloadManager: DownloadManager,
    private val downloadNotifier: DownloadNotifier
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _activeDownloads = MutableStateFlow<List<ActiveDownload>>(emptyList())
    val activeDownloads: StateFlow<List<ActiveDownload>> = _activeDownloads.asStateFlow()

    private val _activeCount = MutableStateFlow(0)
    val activeCount: StateFlow<Int> = _activeCount.asStateFlow()

    private val _averageProgress = MutableStateFlow(0f)
    val averageProgress: StateFlow<Float> = _averageProgress.asStateFlow()

    init {
        observeDownloadEvents()
    }

    private fun observeDownloadEvents() {
        scope.launch {
            downloadManager.downloadEvents.collect { event ->
                when (event) {
                    is DownloadEvent.Started -> {
                        _activeDownloads.update { list ->
                            list + ActiveDownload(event.downloadId, event.title, 0f)
                        }
                        updateDerivedState()
                        downloadNotifier.updateProgress(event.downloadId, event.title, 0f)
                    }
                    is DownloadEvent.Progress -> {
                        val title = _activeDownloads.value
                            .find { it.id == event.downloadId }?.title ?: event.downloadId
                        _activeDownloads.update { list ->
                            list.map { download ->
                                if (download.id == event.downloadId) {
                                    download.copy(progress = event.progress)
                                } else download
                            }
                        }
                        updateDerivedState()
                        downloadNotifier.updateProgress(event.downloadId, title, event.progress)
                    }
                    is DownloadEvent.Completed -> {
                        _activeDownloads.update { list ->
                            list.filter { it.id != event.downloadId }
                        }
                        updateDerivedState()
                        downloadNotifier.showCompleted(event.downloadId, event.title)
                    }
                    is DownloadEvent.Failed -> {
                        _activeDownloads.update { list ->
                            list.filter { it.id != event.downloadId }
                        }
                        updateDerivedState()
                        downloadNotifier.showFailed(event.downloadId, event.title, event.error)
                    }
                }
            }
        }
    }

    private fun updateDerivedState() {
        val downloads = _activeDownloads.value
        _activeCount.value = downloads.size
        _averageProgress.value = if (downloads.isEmpty()) 0f
            else downloads.map { it.progress }.average().toFloat()
    }
}
```

**Step 2: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadStateHolder.kt
git commit -m "feat(download): integrate notifier into DownloadStateHolder"
```

---

## Task 11: Update AppModule for DownloadStateHolder Dependencies

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt`

**Step 1: Update DownloadStateHolder registration**

Change the DownloadStateHolder line to:
```kotlin
    single { DownloadStateHolder(get(), get()) }
```

Also add AppLifecycleObserver registration:
```kotlin
import com.lowiq.jellyfish.domain.download.AppLifecycleObserver
```

And in dataModule:
```kotlin
    single { AppLifecycleObserver(get(), get()) }
```

**Step 2: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt
git commit -m "feat(di): update DI for DownloadStateHolder with notifier dependency"
```

---

## Task 12: Initialize AppLifecycleObserver in Android Application

**Files:**
- Modify: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/JellyFishApplication.kt`

**Step 1: Start lifecycle observer**

Add import:
```kotlin
import com.lowiq.jellyfish.domain.download.AppLifecycleObserver
import org.koin.android.ext.android.inject
```

Add after startKoin:
```kotlin
        val lifecycleObserver: AppLifecycleObserver by inject()
        lifecycleObserver.start()
```

**Step 2: Build to verify compilation**

Run: `./gradlew :composeApp:compileKotlinAndroid`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/JellyFishApplication.kt
git commit -m "feat(android): initialize AppLifecycleObserver on app start"
```

---

## Task 13: Update AndroidManifest with Notification Permission

**Files:**
- Modify: `composeApp/src/androidMain/AndroidManifest.xml`

**Step 1: Add notification permission**

Add after INTERNET permission:
```xml
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

**Step 2: Build to verify**

Run: `./gradlew :composeApp:assembleDebug`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/androidMain/AndroidManifest.xml
git commit -m "feat(android): add notification and foreground service permissions"
```

---

## Task 14: Test the Implementation

**Step 1: Run desktop app**

Run: `./gradlew :composeApp:run`

**Step 2: Verify sidebar indicator appears when download starts**

1. Navigate to a movie/series detail
2. Start a download
3. Verify circular progress appears on Downloads icon in sidebar
4. Verify badge shows count

**Step 3: Run Android app**

Run: `./gradlew :composeApp:installDebug`

**Step 4: Test Android notifications**

1. Start a download
2. Press Home to background the app
3. Verify notification appears with progress
4. Wait for download to complete
5. Verify "Terminé" notification appears and auto-dismisses

**Step 5: Final commit**

```bash
git commit --allow-empty -m "test: verify download indicator and notifications working"
```

---

## Summary

This plan implements:
1. **DownloadStateHolder** - Aggregates download events into observable state
2. **DownloadIndicator** - Composable with circular progress + badge
3. **NavigationRail** - Updated to show indicator on Downloads icon
4. **DownloadNotifier** - Platform-specific notification handling (Android full, others no-op)
5. **AppLifecycleObserver** - Detects foreground/background to enable/disable notifications
6. **DI Integration** - All components registered in Koin
