package com.example.present.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.present.data.database.entities.ChatEntity

@Dao
interface ChatDao {
    @Insert(entity = ChatEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: ChatEntity)

    @Query("SELECT * FROM chat_table")
    fun getChat(): ChatEntity

    @Query("DELETE FROM chat_table")
    fun deleteChatTable()
}