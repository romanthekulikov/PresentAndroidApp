package com.example.present.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.present.data.database.entities.StageEntity

@Dao
interface StageDao {
    @Insert(entity = StageEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveStage(stage: StageEntity)

    @Query("SELECT * FROM stage_table")
    fun getAllStage(): List<StageEntity>

    @Query("SELECT * FROM stage_table WHERE id_stage = :id")
    fun getStageById(id: Int): List<StageEntity>

    @Query("SELECT * FROM stage_table ORDER BY id_stage LIMIT 1")
    fun getLastStage(): StageEntity
}