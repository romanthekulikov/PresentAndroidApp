package com.example.present.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_table")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_chat") var idChat: Int,
    @ColumnInfo(name = "background_color") val bgColor: String,
    @ColumnInfo(name = "background_image") val bgImg: String,
    @ColumnInfo(name = "text_size") val textSize: Int
)
