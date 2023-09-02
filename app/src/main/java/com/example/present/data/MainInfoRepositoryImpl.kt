package com.example.present.data

import android.content.Context
import com.example.present.domain.MainInfoRepository

class MainInfoRepositoryImpl(private val context: Context) : MainInfoRepository {
    override fun getProgress(): Int {
        return Pref(context).getProgress()
    }

    override fun addProgress(newProgress: Int) {
        Pref(context).saveProgress(newProgress)
    }
}