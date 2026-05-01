package com.example.noveltracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noveltracker.data.local.entity.Goal
import com.example.noveltracker.data.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val repository: GoalRepository
) : ViewModel() {

    val goals: StateFlow<List<Goal>> = repository.getAllGoals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addGoal(title: String, totalProgress: Int?, dueDate: Long?) {
        viewModelScope.launch {
            repository.insertGoal(
                Goal(
                    title = title,
                    totalProgress = totalProgress,
                    dueDate = dueDate
                )
            )
        }
    }

    fun updateProgress(goal: Goal, newProgress: Int) {
        viewModelScope.launch {
            repository.updateGoal(goal.copy(currentProgress = newProgress))
        }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }
}
