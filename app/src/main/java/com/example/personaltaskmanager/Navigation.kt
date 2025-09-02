package com.example.personaltaskmanager

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") {
            HomeScreen(
                NavigateToTask = { navController.navigate("Task") },
                NavigateToEvents = { navController.navigate("Events") }

            )
        }
        composable("Task")
        {
            TaskScreen(navController = navController, onBack = { navController.popBackStack() })
        }
        composable("Events")
        {
            EventsScreen(onBack = { navController.popBackStack() })
        }
        composable("AddTask")
        {
            AddTask(onBack = { navController.popBackStack() })
        }

    }
}