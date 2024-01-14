package com.example.present.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.present.data.database.entities.FormItemEntity

@Dao
interface FormItemDao {
    @Insert(entity = FormItemEntity::class)
    fun saveFormItem(formItem: FormItemEntity)

    @Query("SELECT * FROM form_item_table")
    fun getAllFormItems(): List<FormItemEntity>

    @Query("DELETE FROM form_item_table WHERE id_stage = :idStage")
    fun deletePresent(idStage: Int)

    @Query("DELETE FROM form_item_table")
    fun deleteData()
}