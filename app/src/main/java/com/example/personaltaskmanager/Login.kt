package com.example.personaltaskmanager

import android.R.attr.text
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.text.isNotEmpty

@Composable
fun Login(NavigateToLogin: () -> Unit, NavigateToSignUp: () -> Unit, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    val context=LocalContext.current
    Column(

        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", fontSize = 30.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    text = emailError.ifEmpty { "Email" },
                    color = if (emailError.isNotEmpty()) Red else Unspecified
                )

            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    text = passwordError.ifEmpty { "Password" },
                    color = if (passwordError.isNotEmpty()) Red else Unspecified
                )

            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(

            modifier = Modifier.fillMaxWidth(),

            onClick = {
                emailError = when {
                    email.isBlank() -> "Email is required"
                    !isValidEmail(email) -> "Invalid Email"
                    else -> ""
                }
                passwordError = when {
                    password.isBlank() -> "Password is required"
                    password.length < 6 -> "Password must be at least 6 characters"
                    else -> ""
                }
                if (emailError.isEmpty() && passwordError.isEmpty()) {
                    navController.navigate("Home")
                }else
                {
                    Toast.makeText(context, "Login Unsuccessful", Toast.LENGTH_LONG).show()
                }
            }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text("Already have an account? ")
            Text(
                "Sign Up",
                color = Color.Blue,
                modifier = Modifier.clickable { navController.navigate("SignUp") })
        }
    }

  /*      Column {
            Spacer(modifier = Modifier.height(8.dp))
            Text("-----------------or----------------")
        }*/
    }



fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}