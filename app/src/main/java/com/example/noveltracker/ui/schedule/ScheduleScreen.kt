package com.example.noveltracker.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
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
    val selectedDate by viewModel.selectedDate.collectAsState()

    val dateFormat = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = dateFormat.format(Date(selectedDate))) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.previousDay() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Previous Day")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.nextDay() }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Next Day")
                    }
                }
            )
        },
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
                Text(text = "No tasks scheduled for this day.")
            }
        } else {
            // Simple vertical list mimicking a timetable for now
            // To make a true hour-by-hour grid requires a custom layout.
            // Sorting by start time to keep chronological order.
            val sortedPlans = taskPlans.sortedBy { it.taskPlan.startTime }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sortedPlans) { item ->
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
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val startTimeString = timeFormat.format(Date(item.taskPlan.startTime))
    val endTimeString = timeFormat.format(Date(item.taskPlan.startTime + item.taskPlan.durationMinutes * 60000L))

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = startTimeString, style = MaterialTheme.typography.labelMedium)
                Text(text = "|", style = MaterialTheme.typography.labelSmall)
                Text(text = endTimeString, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.goal.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (item.taskPlan.isCompleted) TextDecoration.LineThrough else null
                )
                Text(
                    text = "${item.taskPlan.durationMinutes} mins",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Checkbox(
                checked = item.taskPlan.isCompleted,
                onCheckedChange = { onToggleComplete() }
            )

            IconButton(onClick = onDelete) {
                Text("🗑️") // Simple delete icon for prototype
            }
        }
    }
}
