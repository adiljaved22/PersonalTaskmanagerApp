package com.example.personaltaskmanager

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.messaging.FirebaseMessaging

@Composable

fun ProfileScreen(context: Context, logout: () -> Unit) {
    val sharedPreference= context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val username = sharedPreference.getString("username", "")
    val email = sharedPreference.getString("email", "")
    val profile_image = sharedPreference.getString("profile_image", "")
    val sessionManager = SessionManager(context)
    Log.d("profile pix", "${profile_image}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Image(

            painter = rememberAsyncImagePainter(profile_image),
            contentDescription = "Profile Image",

            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Username: $username", fontSize = 10.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(12.dp))
        Log.d("all things", "${profile_image},${username},${email}")

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Email: $email", fontSize = 10.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            shape = RoundedCornerShape(36), onClick = {
                sessionManager.logout()
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Tutorial")

                sharedPreference.edit().clear().apply()
                Log.e("logout", "FCM TOKEN CLEARED")
                logout()
            }) {

            Text("Logout")
        }
    }
}