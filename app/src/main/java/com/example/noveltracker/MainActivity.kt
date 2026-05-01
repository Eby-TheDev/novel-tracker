package com.example.noveltracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.noveltracker.ui.dashboard.AddGoalDialog
import com.example.noveltracker.ui.dashboard.DashboardScreen
import com.example.noveltracker.ui.dashboard.GoalViewModel
import com.example.noveltracker.ui.schedule.AddPlanDialog
import com.example.noveltracker.ui.schedule.ScheduleScreen
import com.example.noveltracker.ui.schedule.ScheduleViewModel
import com.example.noveltracker.ui.theme.NovelTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovelTrackerTheme {
                MainScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: @Composable () -> Unit) {
    object Dashboard : Screen("dashboard", "Dashboard", { Icon(Icons.Default.Home, contentDescription = null) })
    object Schedule : Screen("schedule", "Schedule", { Icon(Icons.Default.List, contentDescription = null) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var showAddGoalDialog by remember { mutableStateOf(false) }
    var showAddPlanDialog by remember { mutableStateOf(false) }
    val goalViewModel: GoalViewModel = hiltViewModel()
    val scheduleViewModel: ScheduleViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val screens = listOf(Screen.Dashboard, Screen.Schedule)
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = screen.icon,
                        label = { Text(screen.title) },
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = goalViewModel,
                    onAddGoalClick = { showAddGoalDialog = true }
                )
            }
            composable(Screen.Schedule.route) {
                ScheduleScreen(
                    viewModel = scheduleViewModel,
                    onAddPlanClick = { showAddPlanDialog = true }
                )
            }
        }

        if (showAddGoalDialog) {
            AddGoalDialog(
                onDismiss = { showAddGoalDialog = false },
                onConfirm = { title, total, due ->
                    goalViewModel.addGoal(title, total, due)
                    showAddGoalDialog = false
                }
            )
        }

        if (showAddPlanDialog) {
            val goals by scheduleViewModel.goals.collectAsState()
            AddPlanDialog(
                goals = goals,
                onDismiss = { showAddPlanDialog = false },
                onConfirm = { goalId, startTime, duration ->
                    scheduleViewModel.addTaskPlan(goalId, startTime, duration)
                    showAddPlanDialog = false
                }
            )
        }
    }
}
