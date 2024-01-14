package com.example.present.remote.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("user_id")
    val userId: Int,
    val email: String,
    val name: String,
    val icon: String,
    @SerializedName("error")
    val error: Int?,
    @SerializedName("code_message")
    val message: String?
)
