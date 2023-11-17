package com.example.present.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "present_table")
data class PresentEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_present") val id: Int,
    @ColumnInfo(name = "id_stage") val idStage: Int,
    @ColumnInfo(name = "present_text") val congratulation: String,
    @ColumnInfo(name = "present_img") val image: String,
    @ColumnInfo(name = "redirect_link") val link: String,
)
