package com.example.personaltaskmanager

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.toString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEvents(viewModel: TaskViewModel = viewModel(), onBack: () -> Unit) {
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
    var openDialogBox by remember { mutableStateOf(false) }
    var openTimeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Events") }, actions = {
                    IconButton(onClick = { onBack() }) {
                        Icon(imageVector = Icons.Filled.Home, contentDescription = null)
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.teal_700)
                )
            )

        }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),

            )

        {

        }
    }
    Column(

        modifier = Modifier.fillMaxWidth(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(0.9f),
            value = eventName,
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
                viewModel.addEvent(
                    0, eventName, eventLocation, date,
                    time.substring(0, 2).toInt(), time.substring(3, 5).toInt()
                )
                onBack()
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
    if (openDialogBox) {
        DatePickerDialog(onDismissRequest = { openDialogBox = false }, confirmButton = {
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
                time = String.format("%02d:%02d:00", hour, minute)
                openTimeDialog = false
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        ).show()
    }


}