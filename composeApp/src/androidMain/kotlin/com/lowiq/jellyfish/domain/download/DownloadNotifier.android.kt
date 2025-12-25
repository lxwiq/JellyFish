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
