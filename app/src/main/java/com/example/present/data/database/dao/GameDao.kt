package com.example.present.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.present.data.database.entities.GameEntity

@Dao
interface GameDao {
    @Insert(entity = GameEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveGame(game: GameEntity)

    @Query("SELECT * FROM game_table WHERE id_game = :id")
    fun getGameById(id: Int): GameEntity

    @Query("SELECT * FROM game_table")
    fun getLastGame(): GameEntity?

    @Query("DELETE FROM game_table")
    fun deleteGameTable()
}