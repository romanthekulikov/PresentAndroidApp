package com.example.present.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.present.data.database.entities.UserEntity

@Dao
interface UserDao {
    @Insert(entity = UserEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: UserEntity)

    @Query("SELECT * FROM user_table")
    fun getUser(): UserEntity?

    @Query("UPDATE user_table SET icon = :icon, name = :name")
    fun updateUserInfo(name: String, icon: String)

    @Query("DELETE FROM user_table")
    fun deleteUserTable()
}