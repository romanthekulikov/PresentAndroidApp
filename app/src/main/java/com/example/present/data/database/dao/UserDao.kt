package com.example.present.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.present.data.database.entities.UserEntity

@Dao
interface UserDao {
    @Insert(entity = UserEntity::class)
    fun saveUser(user: UserEntity)

    @Query("SELECT * FROM user_table")
    fun getUser(): UserEntity?
}