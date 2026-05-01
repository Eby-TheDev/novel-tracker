package com.example.noveltracker.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noveltracker.data.local.entity.Goal
import com.example.noveltracker.data.local.entity.TaskPlan
import com.example.noveltracker.data.local.entity.TaskPlanWithGoal
import com.example.noveltracker.data.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: GoalRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis)
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    val taskPlans: StateFlow<List<TaskPlanWithGoal>> = combine(
        repository.getAllTaskPlans(),
        _selectedDate
    ) { allPlans, selectedDayStart ->
        val selectedDayEnd = selectedDayStart + 24 * 60 * 60 * 1000 - 1
        allPlans.filter { it.taskPlan.startTime in selectedDayStart..selectedDayEnd }
    }.stateIn(
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

    fun previousDay() {
        _selectedDate.value -= 24 * 60 * 60 * 1000
    }

    fun nextDay() {
        _selectedDate.value += 24 * 60 * 60 * 1000
    }

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
