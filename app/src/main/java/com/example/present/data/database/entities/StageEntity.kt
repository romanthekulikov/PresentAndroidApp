package com.example.present.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "stage_table")
data class StageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_stage") val id: Int,
    @ColumnInfo(name = "id_game") val idGame: Int,
    @ColumnInfo(name = "text_stage") val textStage: String,
    @ColumnInfo(name = "hint_stage") val textHint: String,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "is_done") val isDone: Boolean
)
