package com.example.present.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.present.data.database.entities.PresentEntity

@Dao
interface PresentDao {
    @Insert(entity = PresentEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun savePresent(present: PresentEntity)

    @Query("SELECT * FROM present_table")
    fun getAllPresents(): List<PresentEntity>

    @Query("SELECT * FROM present_table WHERE id_present = :id")
    fun getPresentById(id: Int): PresentEntity

    @Query("DELETE FROM present_table")
    fun deletePresentTable()
}