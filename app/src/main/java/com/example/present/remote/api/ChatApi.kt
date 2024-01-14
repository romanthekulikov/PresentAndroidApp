package com.example.present.remote.api

import com.example.present.remote.model.ChatResponse
import com.example.present.remote.model.RegResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApi {
    @GET("/chat_settings")
    fun getChatSettings(@Query("chat_id") chatId: Int): Call<ChatResponse>

    @POST("/save_message")
    fun saveMessage(@Query("sender_id") senderId: Int,@Query("chat_id") chatId: Int, @Query("text") text: String, @Query("replay_id") replayId: Int?): Call<RegResponse>

    @POST("/update_message")
    fun updateMessage(@Query("text") text: String, @Query("message_id") messageId: Int)

    @POST("/delete_message")
    fun deleteMessage(@Query("message_id") messageId: Int)

    @GET("/messages_by_substring")
    fun getMessageBySubstring(@Query("substring") substring: String, @Query("chat_id") chatId: Int)
}