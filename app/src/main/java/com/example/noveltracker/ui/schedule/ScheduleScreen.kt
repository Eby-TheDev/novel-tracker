package com.example.noveltracker.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.noveltracker.data.local.entity.TaskPlanWithGoal
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    onAddPlanClick: () -> Unit
) {
    val taskPlans by viewModel.taskPlans.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPlanClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Plan")
            }
        }
    ) { padding ->
        if (taskPlans.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No tasks scheduled. Tap + to plan one!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(taskPlans) { item ->
                    TaskPlanItem(
                        item = item,
                        onToggleComplete = { viewModel.toggleTaskCompletion(item.taskPlan) },
                        onDelete = { viewModel.deleteTaskPlan(item.taskPlan) }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskPlanItem(
    item: TaskPlanWithGoal,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(Date(item.taskPlan.startTime))

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.taskPlan.isCompleted,
                onCheckedChange = { onToggleComplete() }
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${timeString} - ${item.goal.title}",
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (item.taskPlan.isCompleted) TextDecoration.LineThrough else null
                )
                Text(
                    text = "Duration: ${item.taskPlan.durationMinutes} mins",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onDelete) {
                Text("🗑️") // Simple delete icon for prototype
            }
        }
    }
}
