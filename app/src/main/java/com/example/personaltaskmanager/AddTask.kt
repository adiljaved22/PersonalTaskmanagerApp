package com.example.personaltaskmanager

import android.util.Log
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
                    label = { Text("Task Name  ") })
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Add Description") })
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Add Location") })
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = {

                    viewModel.addTask(0,name, description, location,false)

                    onBack()
                }) {
                    Text("Add")
                }
            }
        }

    }
}
