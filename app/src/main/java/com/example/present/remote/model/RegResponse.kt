package com.example.present.remote.model

import com.google.gson.annotations.SerializedName

data class RegResponse(
    @SerializedName("error")
    val error: Int,
    @SerializedName("code_message")
    val message: String
)