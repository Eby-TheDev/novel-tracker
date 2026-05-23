package com.example.noveltracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noveltracker.data.local.entity.Goal
import com.example.noveltracker.data.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class GoalFilter {
    ALL, TODO, IN_PROGRESS, COMPLETED
}

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val repository: GoalRepository
) : ViewModel() {

    private val _filterType = MutableStateFlow(GoalFilter.ALL)
    val filterType: StateFlow<GoalFilter> = _filterType.asStateFlow()

    val goals: StateFlow<List<Goal>> = combine(
        repository.getAllGoals(),
        _filterType
    ) { allGoals, filter ->
        when (filter) {
            GoalFilter.ALL -> allGoals
            GoalFilter.TODO -> allGoals.filter { it.currentProgress == 0 }
            GoalFilter.IN_PROGRESS -> allGoals.filter { it.currentProgress > 0 && (it.totalProgress == null || it.currentProgress < it.totalProgress) }
            GoalFilter.COMPLETED -> allGoals.filter { it.totalProgress != null && it.currentProgress >= it.totalProgress }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setFilter(filter: GoalFilter) {
        _filterType.value = filter
    }

    fun addGoal(title: String, currentProgress: Int, totalProgress: Int?, dueDate: Long?) {
        viewModelScope.launch {
            repository.insertGoal(
                Goal(
                    title = title,
                    currentProgress = currentProgress,
                    totalProgress = totalProgress,
                    dueDate = dueDate
                )
            )
        }
    }

    fun updateProgress(goal: Goal, newProgress: Int) {
        val validatedProgress = if (goal.totalProgress != null) {
            newProgress.coerceIn(0, goal.totalProgress)
        } else {
            newProgress.coerceAtLeast(0)
        }

        if (validatedProgress != goal.currentProgress) {
            viewModelScope.launch {
                repository.updateGoal(goal.copy(currentProgress = validatedProgress))
            }
        }
    }

    fun updateGoalDetails(goal: Goal, title: String, currentProgress: Int, totalProgress: Int?, dueDate: Long?) {
        val validatedProgress = if (totalProgress != null) {
            currentProgress.coerceIn(0, totalProgress)
        } else {
            currentProgress.coerceAtLeast(0)
        }
        
        viewModelScope.launch {
            repository.updateGoal(
                goal.copy(
                    title = title,
                    currentProgress = validatedProgress,
                    totalProgress = totalProgress,
                    dueDate = dueDate
                )
            )
        }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }
}
