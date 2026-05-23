package com.example.noveltracker.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.noveltracker.data.local.entity.Goal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalDialog(
    goal: Goal,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int?, Long?) -> Unit
) {
    var title by remember { mutableStateOf(goal.title) }
    var currentProgress by remember { mutableStateOf(goal.currentProgress.toString()) }
    var totalProgress by remember { mutableStateOf(goal.totalProgress?.toString() ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = currentProgress,
                    onValueChange = { currentProgress = it },
                    label = { Text("Current Progress") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = totalProgress,
                    onValueChange = { totalProgress = it },
                    label = { Text("Total Chapters (Optional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        val currentProgInt = currentProgress.toIntOrNull() ?: 0
                        val totalProgInt = totalProgress.toIntOrNull()
                        onConfirm(
                            title,
                            currentProgInt,
                            totalProgInt,
                            goal.dueDate
                        )
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
