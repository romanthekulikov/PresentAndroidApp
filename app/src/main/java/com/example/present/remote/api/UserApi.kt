package com.example.present.remote.api

import com.example.present.remote.model.AuthResponse
import com.example.present.remote.model.OpenPresentResponse
import com.example.present.remote.model.RegResponse
import com.example.present.remote.model.UserFull
import com.example.present.remote.model.UserInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("/reg")
    fun reg(@Query("email") email: String, @Query("password") password: String): Call<RegResponse>

    @POST("/update_user_info")
    fun updateUserInfo(@Query("user_id") userId: Int, @Query("name") name: String, @Query("icon") icon: String?): Call<RegResponse>

    @GET("/auth")
    fun auth(@Query("email") email: String, @Query("password") password: String): Call<AuthResponse>

    @GET("/user")
    fun getUser(@Query("user_id") userId: Int): Call<UserFull>

    @GET("/user_info")
    fun getUserInfo(@Query("email") email: String): Call<UserInfo>
}