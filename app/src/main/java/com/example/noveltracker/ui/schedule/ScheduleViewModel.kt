package com.example.noveltracker.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noveltracker.data.local.entity.Goal
import com.example.noveltracker.data.local.entity.TaskPlan
import com.example.noveltracker.data.local.entity.TaskPlanWithGoal
import com.example.noveltracker.data.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: GoalRepository
) : ViewModel() {

    val taskPlans: StateFlow<List<TaskPlanWithGoal>> = repository.getAllTaskPlans()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val goals: StateFlow<List<Goal>> = repository.getAllGoals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTaskPlan(goalId: Long, startTime: Long, duration: Int) {
        viewModelScope.launch {
            repository.insertTaskPlan(
                TaskPlan(
                    goalId = goalId,
                    startTime = startTime,
                    durationMinutes = duration
                )
            )
        }
    }

    fun toggleTaskCompletion(taskPlan: TaskPlan) {
        viewModelScope.launch {
            repository.updateTaskPlan(taskPlan.copy(isCompleted = !taskPlan.isCompleted))
        }
    }

    fun deleteTaskPlan(taskPlan: TaskPlan) {
        viewModelScope.launch {
            repository.deleteTaskPlan(taskPlan)
        }
    }
}
