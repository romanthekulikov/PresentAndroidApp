package com.example.present.domain

interface MainInfoRepository {
    fun getProgress(): Int

    fun addProgress(newProgress: Int)
}