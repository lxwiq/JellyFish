package com.lowiq.jellyfish.data.local

import java.io.File

actual class FileManager {

    private val baseDir = File(System.getProperty("user.home"), ".jellyfish/downloads")

    actual fun getDownloadsDirectory(): String {
        if (!baseDir.exists()) baseDir.mkdirs()
        return baseDir.absolutePath
    }

    actual fun getAvailableSpaceBytes(): Long {
        return baseDir.usableSpace
    }

    actual fun getUsedSpaceBytes(): Long {
        return baseDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
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
