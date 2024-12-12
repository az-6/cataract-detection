package com.dicoding.capstone.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUri: String, // Disimpan sebagai String URI
    val condition: String,
    val predictionScore: String
)
