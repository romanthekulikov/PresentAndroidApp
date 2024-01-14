package com.example.present.remote.model

import com.google.gson.annotations.SerializedName

data class CreateGameResponse(
    @SerializedName("game_id")
    val gameId: Int,
    @SerializedName("key_enter")
    val keyEnter: String,
    @SerializedName("error")
    val error: Int?,
    @SerializedName("code_message")
    val message: String?
)
