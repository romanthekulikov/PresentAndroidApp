package com.example.present.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.present.data.database.entities.GameEntity

@Dao
interface GameDao {
    @Insert(entity = GameEntity::class)
    fun saveGame(game: GameEntity)

    @Query("SELECT * FROM game_table WHERE id_game = :id")
    fun getGameById(id: Int): GameEntity
}