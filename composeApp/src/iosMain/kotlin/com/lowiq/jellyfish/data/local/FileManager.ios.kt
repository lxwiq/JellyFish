package com.lowiq.jellyfish.data.local

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSURL
import platform.Foundation.NSFileSystemFreeSize
import platform.Foundation.NSNumber

@OptIn(ExperimentalForeignApi::class)
actual class FileManager {

    private val fileManager = NSFileManager.defaultManager

    actual fun getDownloadsDirectory(): String {
        val paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
        val documentsDir = paths.firstOrNull() as? String ?: ""
        val downloadsDir = "$documentsDir/downloads"
        if (!fileManager.fileExistsAtPath(downloadsDir)) {
            fileManager.createDirectoryAtPath(downloadsDir, withIntermediateDirectories = true, attributes = null, error = null)
        }
        return downloadsDir
    }

    actual fun getAvailableSpaceBytes(): Long {
        val paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
        val documentsDir = paths.firstOrNull() as? String ?: return 0
        val attrs = fileManager.attributesOfFileSystemForPath(documentsDir, error = null) ?: return 0
        val freeSize = attrs[NSFileSystemFreeSize] as? NSNumber
        return freeSize?.longLongValue ?: 0
    }

    actual fun getUsedSpaceBytes(): Long {
        val dir = getDownloadsDirectory()
        var totalSize = 0L
        val contents = fileManager.contentsOfDirectoryAtPath(dir, error = null) ?: return 0
        contents.forEach { file ->
            val path = "$dir/$file"
            val attrs = fileManager.attributesOfItemAtPath(path, error = null)
            val size = (attrs?.get("NSFileSize") as? NSNumber)?.longLongValue ?: 0
            totalSize += size
        }
        return totalSize
    }

    actual fun deleteFile(path: String): Boolean {
        return fileManager.removeItemAtPath(path, error = null)
    }

    actual fun fileExists(path: String): Boolean {
        return fileManager.fileExistsAtPath(path)
    }

    actual fun getFileSize(path: String): Long {
        val attrs = fileManager.attributesOfItemAtPath(path, error = null) ?: return 0
        val size = attrs["NSFileSize"] as? NSNumber
        return size?.longLongValue ?: 0
    }
}
