package com.example.personaltaskmanager

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Edit(
    EventsToBeEdit: Events,
    onBack: () -> Unit,
    viewModel: TaskViewModel = viewModel()
) {

    val state = rememberDatePickerState()
    var openDialogBox by rememberSaveable { mutableStateOf(false) }
    var openTimeDialog by rememberSaveable { mutableStateOf(false) }
    var name by remember { mutableStateOf(EventsToBeEdit.event_name) }
    var location by remember { mutableStateOf(EventsToBeEdit.location) }
    var date by remember { mutableStateOf(EventsToBeEdit.event_date) }
    var time by remember { mutableStateOf(EventsToBeEdit.event_time) }



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Edit Event", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = date, onValueChange = { date = it }, label = { Text("Date") },
            enabled = false,
            modifier = Modifier

                .clickable { openDialogBox = true }

        )
        if (openDialogBox) {
            DatePickerDialog(
                onDismissRequest = { openDialogBox = false },
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
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = time, onValueChange = { time = it }, label = { Text("Time") },

            enabled = false,
            modifier = Modifier
                .clickable { openTimeDialog = true })
        val calendar = Calendar.getInstance()
        if (openTimeDialog) {
            TimePickerDialog(
                LocalContext.current, { _, hour: Int, minute: Int ->
                    time = String.format("%02d:%02d", hour, minute)
                    openTimeDialog = false
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
            ).show()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.update(EventsToBeEdit.id, name, location, date, time)

            onBack()
        }) {
            Text("Update")
        }
    }
}

