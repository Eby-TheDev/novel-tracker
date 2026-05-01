package com.example.noveltracker.ui.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.LazyColumn
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

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddGoalClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Goal")
            }
        }
    ) { padding ->
        if (goals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No goals yet. Tap + to add one!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(goals) { goal ->
                    GoalCard(
                        goal = goal,
                        onProgressUpdate = { viewModel.updateProgress(goal, it) },
                        onDelete = { viewModel.deleteGoal(goal) }
                    )
                }
            }
        }
    }
}

@Composable
fun GoalCard(
    goal: Goal,
    onProgressUpdate: (Int) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
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

            if (goal.totalProgress != null) {
                val progressValue = goal.currentProgress.toFloat() / goal.totalProgress
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
                TextButton(onClick = { onProgressUpdate(goal.currentProgress + 1) }) {
                    Text("+1 Progress")
                }
                TextButton(onClick = onDelete) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
