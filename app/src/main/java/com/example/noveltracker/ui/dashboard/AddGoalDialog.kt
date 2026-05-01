package com.example.noveltracker.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int?, Long?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var currentProgress by remember { mutableStateOf("") }
    var totalProgress by remember { mutableStateOf("") }
    // For simplicity in this prototype, we'll just use a Long for due date or skip it for now.
    // In a real app, we'd use a DatePicker.
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Goal") },
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
                    label = { Text("Current Progress (Chapters read)") },
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
                        val initialProgress = currentProgress.toIntOrNull() ?: 0
                        onConfirm(
                            title,
                            initialProgress,
                            totalProgress.toIntOrNull(),
                            null // Due date omitted for simplicity in basic dialog
                        )
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
