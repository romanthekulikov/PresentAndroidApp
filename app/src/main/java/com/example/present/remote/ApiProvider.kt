package com.example.present.remote

import com.example.present.remote.api.ChatApi
import com.example.present.remote.api.GameApi
import com.example.present.remote.api.UserApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiProvider {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://presentback-production.up.railway.app")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userApi = retrofit.create<UserApi>()
    val gameApi = retrofit.create<GameApi>()
    val chatApi = retrofit.create<ChatApi>()
}