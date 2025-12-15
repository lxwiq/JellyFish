package com.lowiq.jellyfish

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform