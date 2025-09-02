package com.example.personaltaskmanager

import android.R.attr.navigationIcon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults.colors
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(onBack: () -> Unit, navController: NavController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task") },
                actions = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.teal_700)
                )
            )
        }
    ) { it ->
        Row(modifier = Modifier.padding(it)) {
            val tabItems = listOf(
                TabItem(
                    title = "Pending Tasks",
                    unSelectedIcon = Icons.Outlined.Pending,
                    selectedIcon = Icons.Filled.Pending
                ),
                TabItem(
                    title = "Completed Tasks",
                    unSelectedIcon = Icons.Outlined.AddTask,
                    selectedIcon = Icons.Filled.AddTask
                )

            )
            Column(modifier = Modifier.fillMaxSize()) {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabItems.forEachIndexed { index, item ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = {
                                selectedTabIndex = index
                            },
                            text = {
                                Text(text = item.title)
                            },
                            icon = {
                                Icon(imageVector = if (index==selectedTabIndex)
                                {
                                    item.selectedIcon
                                }
                                else item.unSelectedIcon,
                                    contentDescription = null
                                )
                            }

                        )
                    }
                }

            }

        }

    }

    Box(contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(
            modifier = Modifier.padding(20.dp),
            contentColor = Color.White,
            containerColor = colorResource(id = R.color.teal_700),
            onClick = {
                navController.navigate("AddTask")
            }) {

            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }

}