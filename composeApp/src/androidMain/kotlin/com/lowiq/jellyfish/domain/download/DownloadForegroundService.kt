package com.lowiq.jellyfish.domain.download

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.lowiq.jellyfish.MainActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject

class DownloadForegroundService : Service() {

    private val downloadManager: DownloadManager by inject()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var progressJob: Job? = null

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "download_service"
        private const val CHANNEL_NAME = "Téléchargements en cours"

        const val ACTION_START = "com.lowiq.jellyfish.START_DOWNLOAD_SERVICE"
        const val ACTION_STOP = "com.lowiq.jellyfish.STOP_DOWNLOAD_SERVICE"

        private var isRunning = false

        fun start(context: Context) {
            if (!isRunning) {
                val intent = Intent(context, DownloadForegroundService::class.java).apply {
                    action = ACTION_START
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            }
        }

        fun stop(context: Context) {
            if (isRunning) {
                val intent = Intent(context, DownloadForegroundService::class.java).apply {
                    action = ACTION_STOP
                }
                context.startService(intent)
            }
        }

        fun isServiceRunning() = isRunning
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startForegroundMode()
            ACTION_STOP -> stopForegroundMode()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundMode() {
        isRunning = true
        val notification = createNotification("Préparation du téléchargement...", 0, 0)
        startForeground(NOTIFICATION_ID, notification)
        observeDownloads()
    }

    private fun stopForegroundMode() {
        isRunning = false
        progressJob?.cancel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        stopSelf()
    }

    private fun observeDownloads() {
        progressJob?.cancel()
        progressJob = scope.launch {
            var activeCount = 0
            var currentTitle = ""
            var currentProgress = 0f

            downloadManager.downloadEvents.collectLatest { event ->
                when (event) {
                    is DownloadEvent.Started -> {
                        activeCount++
                        currentTitle = event.title
                        currentProgress = 0f
                        updateNotification(currentTitle, currentProgress, activeCount)
                    }
                    is DownloadEvent.Progress -> {
                        currentProgress = event.progress
                        updateNotification(currentTitle, currentProgress, activeCount)
                    }
                    is DownloadEvent.Completed -> {
                        activeCount = maxOf(0, activeCount - 1)
                        if (activeCount == 0) {
                            showCompletedNotification(event.title)
                            delay(1000)
                            stopForegroundMode()
                        }
                    }
                    is DownloadEvent.Failed -> {
                        activeCount = maxOf(0, activeCount - 1)
                        if (activeCount == 0) {
                            showFailedNotification(event.title, event.error)
                            delay(1000)
                            stopForegroundMode()
                        }
                    }
                }
            }
        }
    }

    private fun updateNotification(title: String, progress: Float, activeCount: Int) {
        val notification = createNotification(title, (progress * 100).toInt(), activeCount)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotification(title: String, progress: Int, activeCount: Int): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val contentText = if (activeCount > 1) {
            "$progress% • $activeCount téléchargements"
        } else {
            "$progress%"
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(title)
            .setContentText(contentText)
            .setProgress(100, progress, false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .build()
    }

    private fun showCompletedNotification(title: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle(title)
            .setContentText("Téléchargement terminé")
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID + 1, notification)
    }

    private fun showFailedNotification(title: String, error: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle(title)
            .setContentText("Échec: $error")
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID + 2, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Affiche la progression des téléchargements"
                setShowBadge(false)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        progressJob?.cancel()
        scope.cancel()
    }
}
