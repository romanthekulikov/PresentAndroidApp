package com.example.present.remote.model

data class ChatResponse(
    val id: Int,
    val bgColor: String = "default",
    val bgImage: String = "default",
    val textSize: Int = 12
)
