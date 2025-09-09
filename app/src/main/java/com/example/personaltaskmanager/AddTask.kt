package com.example.personaltaskmanager

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.colorResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTask(onBack: () -> Unit, viewModel: TaskViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var locationError by remember { mutableStateOf("") }
    var descriptionError by remember { mutableStateOf("") }
    val context = LocalContext.current


    Scaffold(
        topBar =
            {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = Color.White,
                        containerColor = colorResource(id = R.color.teal_700),
                    ),

                    title = { Text("Add Task") })
            },

        ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {


            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(
                            text = nameError.ifEmpty { "Name" },
                            color = if (nameError.isNotEmpty()) Red else Unspecified
                        )
                    })
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    value = description,
                    onValueChange = { description = it },
                    label = {
                        Text(
                            text = descriptionError.ifEmpty { "description" },
                            color = if (descriptionError.isNotEmpty()) Red else Unspecified
                        )
                    })
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    value = location,
                    onValueChange = { location = it },
                    label = {
                        Text(
                            text = locationError.ifEmpty { "location" },
                            color = if (locationError.isNotEmpty()) Red else Unspecified
                        )
                    })
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = {
                    nameError = when {
                        name.isBlank() -> " Enter Name"
                        else -> ""
                    }
                    descriptionError = when {
                        description.isBlank() -> "Enter Description"
                        else -> ""
                    }
                    locationError = when {
                        location.isBlank() -> "Enter Location"
                        else -> ""
                    }
                    if (name.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty()) {
                        viewModel.addTask(0, name, description, location, false)
                        onBack()
                    } else {
                        Toast.makeText(context, "Fill All the Fields", Toast.LENGTH_SHORT)
                            .show()
                    }

                }) {
                    Text("Add")
                }
            }
        }

    }
}
