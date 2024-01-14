package com.example.present.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_table")
data class GameEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_game") val id: Int,
    @ColumnInfo(name = "id_admin") val idAdmin: Int,
    @ColumnInfo(name = "id_user") val idUser: Int,
    @ColumnInfo(name = "start_date") val startDate: String,
    @ColumnInfo(name = "id_chat") val idChat: Int
)
