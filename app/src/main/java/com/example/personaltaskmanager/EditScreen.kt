package com.example.personaltaskmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Edit(
    EventsToBeEdit: Events,
    onBack: () -> Unit,
    viewModel: TaskViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

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
        OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Time") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.update(EventsToBeEdit.id, name, location, date, time)

            onBack()
        }) {
            Text("Update")
        }
    }
}

