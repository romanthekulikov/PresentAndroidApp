package com.example.present.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "form_item_table")
data class FormItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_stage") var idStage: Int,
    @ColumnInfo(name = "id_user") val idUser: Int,
    @ColumnInfo(name = "text_stage") val textStage: String,
    @ColumnInfo(name = "hint_text") val hintText: String,
    @ColumnInfo(name = "long") val longitude: Double,
    @ColumnInfo(name = "lat") val latitude: Double,
    @ColumnInfo(name = "present_text") val congratulation: String,
    @ColumnInfo(name = "present_img") val presentImg: String,
    @ColumnInfo(name = "key") val key: String,
    @ColumnInfo(name = "key_open") val keyOpen: String,
    @ColumnInfo(name = "redirect_url") val link: String
)
