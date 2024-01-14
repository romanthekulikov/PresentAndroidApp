package com.example.present.domain.mainUseCases

import android.content.Context
import com.example.present.data.database.AppDatabase
import com.example.present.data.database.entities.StageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetDatabase(private val context: Context) {
    fun execute(): AppDatabase {
        return AppDatabase.getDB(context = context)
    }
}