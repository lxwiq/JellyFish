package com.lowiq.jellyfish.data.local

import android.content.Context
import java.io.File

actual class FileManager(private val context: Context) {

    actual fun getDownloadsDirectory(): String {
        val dir = File(context.filesDir, "downloads")
        if (!dir.exists()) dir.mkdirs()
        return dir.absolutePath
    }

    actual fun getAvailableSpaceBytes(): Long {
        return context.filesDir.usableSpace
    }

    actual fun getUsedSpaceBytes(): Long {
        val dir = File(getDownloadsDirectory())
        return dir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }

    actual fun deleteFile(path: String): Boolean {
        return File(path).delete()
    }

    actual fun fileExists(path: String): Boolean {
        return File(path).exists()
    }

    actual fun getFileSize(path: String): Long {
        return File(path).length()
    }
}
