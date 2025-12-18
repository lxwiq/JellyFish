package com.lowiq.jellyfish.domain.model

data class Server(
    val id: String,
    val name: String,
    val url: String,
    val userId: String? = null,
    val username: String? = null
)
