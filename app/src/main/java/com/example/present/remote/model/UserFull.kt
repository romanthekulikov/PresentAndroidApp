package com.example.present.remote.model

data class UserFull (
    val id: Int = -1,
    val password: String,
    val email: String,
    val icon: String,
    val name: String
)