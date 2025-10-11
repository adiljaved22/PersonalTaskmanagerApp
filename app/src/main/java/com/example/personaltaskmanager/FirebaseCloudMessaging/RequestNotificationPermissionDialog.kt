package com.example.personaltaskmanager.FirebaseCloudMessaging



import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog(
    modifier: Modifier = Modifier,
    openDialog: MutableState<Boolean>,
    permissionState: PermissionState
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                permissionState.launchPermissionRequest()
            },
            title = { Text("Notification Permission") },
            text = {
                Text("This app requires notification permission to show notifications.")
            },
            icon = {
                /*Icon(
                    painter = painterResource(),
                    contentDescription = null
                )*/
            },
            confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    permissionState.launchPermissionRequest()
                }) {
                    Text(text = "OK")
                }
            }
        )
    }
}