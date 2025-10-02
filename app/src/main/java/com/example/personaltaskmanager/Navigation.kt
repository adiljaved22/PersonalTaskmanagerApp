
package com.example.personaltaskmanager

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@Composable
fun Navigation(viewModel: TaskViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            Login(
                NavigateToLogin = {
                    navController.navigate("Home") {
                        popUpTo("login") {
                            inclusive = true
                        }
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
                onBack = {
                    navController.navigate("login") {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop=true
                    }

                },
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
        composable(
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
         /*   val context = LocalContext.current
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )*/

            ProfileScreen(
             /*   sharedPreferences = sharedPreferences,*/
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
