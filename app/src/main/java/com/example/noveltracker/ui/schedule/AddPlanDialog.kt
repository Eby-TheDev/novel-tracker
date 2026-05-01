package com.example.noveltracker.ui.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.noveltracker.data.local.entity.Goal
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlanDialog(
    goals: List<Goal>,
    selectedDate: Long, // Pass the currently viewed date
    onDismiss: () -> Unit,
    onConfirm: (Long, Long, Int) -> Unit
) {
    var selectedGoal by remember { mutableStateOf<Goal?>(null) }
    var duration by remember { mutableStateOf("30") }
    var hour by remember { mutableStateOf("12") }
    var minute by remember { mutableStateOf("00") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Plan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box {
                    OutlinedTextField(
                        value = selectedGoal?.title ?: "Select Goal",
                        onValueChange = {},
                        label = { Text("Goal") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        goals.forEach { goal ->
                            DropdownMenuItem(
                                text = { Text(goal.title) },
                                onClick = {
                                    selectedGoal = goal
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = hour,
                        onValueChange = { hour = it },
                        label = { Text("Hour (0-23)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = minute,
                        onValueChange = { minute = it },
                        label = { Text("Minute") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration (minutes)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val goalId = selectedGoal?.id
                    val durInt = duration.toIntOrNull()
                    val hInt = hour.toIntOrNull()?.coerceIn(0, 23) ?: 12
                    val mInt = minute.toIntOrNull()?.coerceIn(0, 59) ?: 0
                    
                    if (goalId != null && durInt != null) {
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = selectedDate
                            set(Calendar.HOUR_OF_DAY, hInt)
                            set(Calendar.MINUTE, mInt)
                            set(Calendar.SECOND, 0)
                        }
                        onConfirm(goalId, calendar.timeInMillis, durInt)
                    }
                },
                enabled = selectedGoal != null
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
