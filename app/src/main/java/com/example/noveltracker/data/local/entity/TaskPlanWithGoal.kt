package com.example.noveltracker.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TaskPlanWithGoal(
    @Embedded val taskPlan: TaskPlan,
    @Relation(
        parentColumn = "goalId",
        entityColumn = "id"
    )
    val goal: Goal
)
