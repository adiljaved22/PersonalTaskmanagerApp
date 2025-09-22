package com.example.personaltaskmanager

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SignUp(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Sign Up", fontSize = 30.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Create a new account", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = {
                Text(
                    text = nameError.ifEmpty { "Name" },
                    color = if (nameError.isNotEmpty()) Red else Unspecified
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
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
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    text = passwordError.ifEmpty { "Password" },
                    color = if (passwordError.isNotEmpty()) Red else Unspecified
                )
            },
            visualTransformation =
                if (passwordVisible) {
                    VisualTransformation.None

                } else {
                    PasswordVisualTransformation('*')
                },
            trailingIcon = {
                val visibilityIcon =
                    if (passwordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = visibilityIcon, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = {
                Text(
                    text = confirmPasswordError.ifEmpty { "Confirm Password" },
                    color = if (confirmPasswordError.isNotEmpty()) Red else Unspecified
                )
            },
            visualTransformation =
                if (passwordVisible) {
                    VisualTransformation.None

                } else {
                    PasswordVisualTransformation('*')
                },
            trailingIcon = {
                val visibilityIcon =
                    if (passwordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = visibilityIcon, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                nameError = when {
                    name.isBlank() -> "Name is required"
                    else -> ""
                }
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
                confirmPasswordError = when {
                    confirmPassword.isBlank() -> "Confirm Password is required"
                    confirmPassword != password -> "Passwords do not match"
                    else -> ""
                }

                if (nameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty() && confirmPasswordError.isEmpty()) {
                    Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, "Sign Up Unsuccessful", Toast.LENGTH_LONG).show()
                }


            }) {
            Text("Sign Up")
        }

    }


}

