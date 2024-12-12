package com.dicoding.capstone.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResultDao {
    @Insert
    suspend fun insertResult(result: ResultEntity)

    @Query("SELECT * FROM results")
    suspend fun getAllResults(): List<ResultEntity>
}

