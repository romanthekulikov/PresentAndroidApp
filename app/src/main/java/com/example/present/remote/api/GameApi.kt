package com.example.present.remote.api

import com.example.present.activities.startPack.gameSignInPack.SignInGameActivity
import com.example.present.remote.model.CreateGameResponse
import com.example.present.remote.model.GamesProgressResponse
import com.example.present.remote.model.OpenPresentResponse
import com.example.present.remote.model.RegResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface GameApi {
    @POST("/create_game")
    fun createGame(@Query("json") json: String): Call<CreateGameResponse>

    @GET("/enter_the_game")
    fun enterTheGame(@Query("id_user") idUser: Int, @Query("key") key: String): Call<SignInGameActivity.EnterResponse>

    @GET("/games_progress")
    fun getGamesProgress(@Query("user_id") userId: Int): Call<List<GamesProgressResponse>>

    @GET("/open_present")
    fun getPresentKey(@Query("present_id") presentId: Int): Call<OpenPresentResponse>

    @POST("/check_stage_key")
    fun checkStageKey(@Query("stage_id") stageId: Int, @Query("key") key: String): Call<RegResponse>
}