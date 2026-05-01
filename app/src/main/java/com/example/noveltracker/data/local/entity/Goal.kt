package com.example.noveltracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val currentProgress: Int = 0,
    val totalProgress: Int? = null,
    val dueDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
