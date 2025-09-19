package com.example.personaltaskmanager

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation(viewModel: TaskViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login"){
            Login(
                NavigateToLogin = { navController.navigate("Home") },
                NavigateToSignUp = { navController.navigate("SignUp") }
                ,navController = navController
            )
        }
        composable("SignUp") {
            SignUp(navController = navController)
        }
        composable("Home") {
            HomeScreen(
                NavigateToTask = { navController.navigate("Task") },
                NavigateToEvents = { navController.navigate("Events") }

            )
        }
        composable("Task")
        {
            TaskScreen(
                navController = navController,
                onBack = {
                    navController.popBackStack() })
        }
        composable("Events")
        {
            EventsScreen(
                navController = navController, onBack = {
                    navController.popBackStack()
                },
            )
        }
        composable("edit/{firestoreId}") { backStackEntry ->
            val firestoreId = backStackEntry.arguments?.getString("firestoreId") ?: ""
            val eventToEdit = viewModel.getEventByFirestoreId(firestoreId)
            if (eventToEdit != null) {
                Edit(
                    EventsToBeEdit = eventToEdit,
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            } else {
                Text("Event not found")
            }
        }

        /*composable(
            "Edit/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { entry ->
            val taskId = entry.arguments!!.getInt("taskId")
            val event = viewModel.getEventById(taskId)
            event?.let {
                Edit(
                    EventsToBeEdit = it,
                    onBack = { navController.popBackStack() },

                )
            }
        }*/
        composable("AddTask")
        {
            AddTask(
                onBack = {
                    navController.navigate("Home")
                    {
                        popUpTo(0)
                        {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onBackClick = { navController.popBackStack() })
        }
        composable("AddEvents") {
            AddEvents(
                onBack = {
                    navController.navigate("Home")

                    {
                        popUpTo(0)
                        {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }

                },
                onBackCLick = { navController.popBackStack() }
            )
        }

    }
}