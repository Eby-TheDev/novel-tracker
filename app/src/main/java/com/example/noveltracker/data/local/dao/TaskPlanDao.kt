package com.example.noveltracker.data.local.dao

import androidx.room.*
import com.example.noveltracker.data.local.entity.TaskPlan
import com.example.noveltracker.data.local.entity.TaskPlanWithGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskPlanDao {
    @Transaction
    @Query("SELECT * FROM task_plans ORDER BY startTime ASC")
    fun getAllTaskPlansWithGoal(): Flow<List<TaskPlanWithGoal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskPlan(taskPlan: TaskPlan): Long

    @Update
    suspend fun updateTaskPlan(taskPlan: TaskPlan)

    @Delete
    suspend fun deleteTaskPlan(taskPlan: TaskPlan)
}
