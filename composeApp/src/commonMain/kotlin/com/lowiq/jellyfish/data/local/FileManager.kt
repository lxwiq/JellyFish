package com.lowiq.jellyfish.data.local

expect class FileManager {
    fun getDownloadsDirectory(): String
    fun getAvailableSpaceBytes(): Long
    fun getUsedSpaceBytes(): Long
    fun deleteFile(path: String): Boolean
    fun fileExists(path: String): Boolean
    fun getFileSize(path: String): Long
}
