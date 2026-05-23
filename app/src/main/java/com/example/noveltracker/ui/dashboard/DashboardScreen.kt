package com.example.noveltracker.ui.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.noveltracker.data.local.entity.Goal
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: GoalViewModel,
    onAddGoalClick: () -> Unit
) {
    val goals by viewModel.goals.collectAsState()
    val currentFilter by viewModel.filterType.collectAsState()
    var editingGoal by remember { mutableStateOf<Goal?>(null) }

    if (editingGoal != null) {
        EditGoalDialog(
            goal = editingGoal!!,
            onDismiss = { editingGoal = null },
            onConfirm = { title, currentProgress, totalProgress, dueDate ->
                viewModel.updateGoalDetails(editingGoal!!, title, currentProgress, totalProgress, dueDate)
                editingGoal = null
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddGoalClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Goal")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(GoalFilter.values()) { filter ->
                    FilterChip(
                        selected = currentFilter == filter,
                        onClick = { viewModel.setFilter(filter) },
                        label = {
                            Text(
                                text = when (filter) {
                                    GoalFilter.ALL -> "All"
                                    GoalFilter.TODO -> "To Do"
                                    GoalFilter.IN_PROGRESS -> "In Progress"
                                    GoalFilter.COMPLETED -> "Completed"
                                }
                            )
                        }
                    )
                }
            }

            if (goals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No goals found.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(goals) { goal ->
                        GoalCard(
                            goal = goal,
                            onProgressUpdate = { viewModel.updateProgress(goal, it) },
                            onDelete = { viewModel.deleteGoal(goal) },
                            onCardClick = { editingGoal = goal }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GoalCard(
    goal: Goal,
    onProgressUpdate: (Int) -> Unit,
    onDelete: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = goal.title, style = MaterialTheme.typography.titleLarge)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val progressText = if (goal.totalProgress != null) {
                "Progress: ${goal.currentProgress} / ${goal.totalProgress}"
            } else {
                "Progress: ${goal.currentProgress}"
            }
            Text(text = progressText, style = MaterialTheme.typography.bodyMedium)

            if (goal.totalProgress != null && goal.totalProgress > 0) {
                val progressValue = (goal.currentProgress.toFloat() / goal.totalProgress).coerceIn(0f, 1f)
                val animatedProgress by animateFloatAsState(
                    targetValue = progressValue,
                    label = "progressAnimation"
                )
                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            goal.dueDate?.let {
                val date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(it))
                Text(
                    text = "Due: $date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                val isCompleted = goal.totalProgress != null && goal.currentProgress >= goal.totalProgress
                TextButton(
                    onClick = { onProgressUpdate(goal.currentProgress + 1) },
                    enabled = !isCompleted
                ) {
                    Text("+1 Progress")
                }
                TextButton(onClick = onDelete) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
