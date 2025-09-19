package com.example.personaltaskmanager

import android.R.attr.onClick
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

    val context=LocalContext.current

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
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = {
                Text(
                    text = confirmPasswordError.ifEmpty { "Password" },
                    color = if (confirmPasswordError.isNotEmpty()) Red else Unspecified
                )
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
                    password.isBlank() -> "password is required"
                    password.length < 6 -> "Password must be at least 6 characters"
                    else -> ""
                }
                confirmPassword = when {
                    confirmPassword.isBlank() -> "Confirm Password is required"
                    password.length < 6 -> "Password must be at least 6 characters"
                    else -> ""
                }

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_LONG).show()

                } else if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_LONG).show()
                }


                navController.popBackStack()
            }) {
            Text("Sign Up")
        }

    }


}


/*


@Composable
fun GoogleSignInButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Icon",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Sign in with Google",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
*/
