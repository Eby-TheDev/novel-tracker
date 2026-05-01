package com.example.noveltracker.data.repository

import com.example.noveltracker.data.local.dao.GoalDao
import com.example.noveltracker.data.local.dao.TaskPlanDao
import com.example.noveltracker.data.local.entity.Goal
import com.example.noveltracker.data.local.entity.TaskPlan
import com.example.noveltracker.data.local.entity.TaskPlanWithGoal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepository @Inject constructor(
    private val goalDao: GoalDao,
    private val taskPlanDao: TaskPlanDao
) {
    // Goal operations
    fun getAllGoals(): Flow<List<Goal>> = goalDao.getAllGoals()
    
    suspend fun insertGoal(goal: Goal) = goalDao.insertGoal(goal)
    
    suspend fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)
    
    suspend fun deleteGoal(goal: Goal) = goalDao.deleteGoal(goal)

    // TaskPlan operations
    fun getAllTaskPlans(): Flow<List<TaskPlanWithGoal>> = taskPlanDao.getAllTaskPlansWithGoal()
    
    suspend fun insertTaskPlan(taskPlan: TaskPlan) = taskPlanDao.insertTaskPlan(taskPlan)
    
    suspend fun updateTaskPlan(taskPlan: TaskPlan) = taskPlanDao.updateTaskPlan(taskPlan)
    
    suspend fun deleteTaskPlan(taskPlan: TaskPlan) = taskPlanDao.deleteTaskPlan(taskPlan)
}
