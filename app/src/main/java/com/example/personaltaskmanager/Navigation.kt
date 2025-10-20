package com.example.personaltaskmanager

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation(viewModel: TaskViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Login") {
        composable("Login") {

            Login(
                NavigateToLogin = {
                    navController.navigate("Home") {
                        popUpTo("Login") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },

                NavigateToSignUp = { navController.navigate("SignUp") },
                navController = navController
            )
        }
        composable("SignUp") {
            SignUp(navController = navController)
        }
        composable("Home") {
            HomeScreen(

                NavigateToTask = { navController.navigate("Task") },
                NavigateToEvents = { navController.navigate("Events") },
                navController = navController,


                /*  logout = {
                      navController.navigate("login") {
                          popUpTo(0) {
                              inclusive = true
                          }
                          launchSingleTop = true
                      }
                  }*/


            )
        }
        composable("Task")
        {
            TaskScreen(
                navController = navController,
                onBack = {
                    navController.popBackStack()
                })
        }
        composable("Events")
        {
            EventsScreen(
                navController = navController,
                onBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(
            "Edit/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { entry ->
            val taskId = entry.arguments!!.getInt("taskId")
            val comingEvents by viewModel.comingEvent.collectAsState()
            val pastEvents by viewModel.pastEvent.collectAsState()
            val allEvents = comingEvents + pastEvents
            val event = allEvents.find { it.id == taskId }
            if (
                event == null
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                )
                {
                    Text("Loading events......")
                }
            } else
                Edit(
                    EventsToBeEdit = event,
                    onBack = { navController.popBackStack() },

                    )

        }

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
        composable("ProfileScreen") {


            ProfileScreen(
                context = LocalContext.current,
                logout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
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
