package com.example.noveltracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noveltracker.data.local.dao.GoalDao
import com.example.noveltracker.data.local.dao.TaskPlanDao
import com.example.noveltracker.data.local.entity.Goal
import com.example.noveltracker.data.local.entity.TaskPlan

@Database(entities = [Goal::class, TaskPlan::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun taskPlanDao(): TaskPlanDao
}
