package com.example.personaltaskmanager

import android.app.TimePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEvents(viewModel: TaskViewModel = viewModel(), onBack: () -> Unit, onBackCLick: () -> Unit) {
    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var locationError by remember { mutableStateOf("") }
    val context = LocalContext.current
    var dateError by remember { mutableStateOf("") }
    var timeError by remember { mutableStateOf("") }
    val state = rememberDatePickerState()
    var openDialogBox by rememberSaveable { mutableStateOf(false) }
    var openTimeDialog by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    if (showDialog) {
        InfoDialog(
            title = "Ahhh!!!",
            desc = "No internet\n Check your internet and try again",
            onDismiss = { showDialog = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = Color.White,
                        containerColor = colorResource(id = R.color.teal_700),
                    ),
                    title = { Text("Add Events") },
                    actions = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                )


            }) { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {


                Column(
                    modifier = Modifier.padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                    )

                {
                    TextField(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        value = eventName,
                        singleLine = true,
                        onValueChange = { eventName = it },
                        label = {
                            Text(

                                text = nameError.ifEmpty { "Name" },
                                color = if (nameError.isNotEmpty()) Red else Unspecified

                            )
                        })
                    Spacer(modifier = Modifier.height(15.dp))
                    TextField(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        value = eventLocation,
                        singleLine = true,
                        onValueChange = { eventLocation = it },
                        label = {
                            Text(
                                text = locationError.ifEmpty { "Location" },
                                color = if (locationError.isNotEmpty()) Red else Unspecified
                            )
                        })
                    Spacer(modifier = Modifier.height(15.dp))
                    TextField(
                        value = date,
                        singleLine = true,
                        onValueChange = { date = it },
                        label = {
                            Text(
                                text = dateError.ifEmpty { "Date" },
                                color = if (dateError.isNotEmpty()) Red else Unspecified
                            )
                        },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clickable { openDialogBox = true })
                    Spacer(modifier = Modifier.height(15.dp))
                    TextField(
                        value = time,
                        singleLine = true,
                        onValueChange = { time = it },
                        label = {
                            Text(
                                text = timeError.ifEmpty { "Time" },
                                color = if (timeError.isNotEmpty()) Red else Unspecified
                            )
                        },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clickable { openTimeDialog = true })
                    Spacer(modifier = Modifier.height(15.dp))

                    Button(onClick = {
                        nameError = when {
                            eventName.isBlank() -> " Enter Name"
                            else -> ""
                        }
                        locationError = when {
                            eventLocation.isBlank() -> "Enter Location"
                            else -> ""
                        }
                        dateError = when {
                            date.isBlank() -> "Enter Date"
                            else -> ""
                        }
                        timeError = when {
                            time.isBlank() -> "Enter Time"
                            else -> ""
                        }
                        if (eventName.isNotEmpty() && eventLocation.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
                            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                                    as ConnectivityManager
                            val networkInfo = cm.activeNetworkInfo
                            if (networkInfo != null && networkInfo.isConnected) {

                                /* val LocalDate = LocalDate.parse(date, formatedDate)
                             val LocalTime = LocalTime.parse(time, formatedTime)*/
                                viewModel.addEvent(
                                    0,
                                    eventName,
                                    eventLocation,
                                    date,
                                    time

                                )

                                onBackCLick()
                                Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                showDialog = true
                            }
                        } else {
                            Toast.makeText(context, "Fill All the Fields", Toast.LENGTH_SHORT)
                                .show()
                        }


                    }

                    )
                    {

                        Text("Add")

                    }
                }
            }

        }

        if (openDialogBox) {
            DatePickerDialog(onDismissRequest = { openDialogBox = false },
                confirmButton = {
                TextButton(onClick = {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    date = sdf.format(Date(state.selectedDateMillis ?: 0L))

                    openDialogBox = false
                }) {
                    Text("OK")
                }

            }, dismissButton = {
                TextButton(onClick = { openDialogBox = false }) {
                    Text("Cancel")
                }
            }) {
                DatePicker(state = state)
            }
        }
        val calendar = Calendar.getInstance()
        if (openTimeDialog) {
            TimePickerDialog(
                LocalContext.current, { _, hour: Int, minute: Int ->
                    time = String.format("%02d:%02d", hour, minute)
                    openTimeDialog = false
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
            ).show()
        }
    }

}
