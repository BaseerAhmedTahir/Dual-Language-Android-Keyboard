package com.example.urduenglishkeyboard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val frequency: Int,
    val language: String // "en" or "ur"
)
