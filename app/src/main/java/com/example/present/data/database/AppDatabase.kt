package com.example.present.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.present.data.database.dao.FormItemDao
import com.example.present.data.database.dao.GameDao
import com.example.present.data.database.dao.PresentDao
import com.example.present.data.database.dao.StageDao
import com.example.present.data.database.dao.UserDao
import com.example.present.data.database.entities.FormItemEntity
import com.example.present.data.database.entities.GameEntity
import com.example.present.data.database.entities.PresentEntity
import com.example.present.data.database.entities.StageEntity
import com.example.present.data.database.entities.UserEntity

@Database(
    version = 1,
    entities = [
        UserEntity::class, GameEntity::class, StageEntity::class,
        PresentEntity::class, FormItemEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getGameDao(): GameDao
    abstract fun getPresentDao(): PresentDao
    abstract fun getStageDao(): StageDao
    abstract fun getFormItemDao(): FormItemDao

    companion object {
        fun getDB(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context = context.applicationContext,
                klass = AppDatabase::class.java,
                name = "AppDB"
            ).build()
        }
    }

}