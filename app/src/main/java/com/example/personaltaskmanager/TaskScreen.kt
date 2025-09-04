package com.example.personaltaskmanager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    onBack: () -> Unit,
    navController: NavController,
    viewModel: TaskViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }
    val pending by viewModel.pendingTask.collectAsState()
    val completed by viewModel.completedTasks.collectAsState()
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

    val pagerState = rememberPagerState(pageCount = { tabItems.size })
    val coroutineScope = rememberCoroutineScope()

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
        },
        floatingActionButton = {
            FloatingActionButton(
                contentColor = Color.White,
                containerColor = colorResource(id = R.color.teal_700),
                onClick = { navController.navigate("AddTask") }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabItems.forEachIndexed { index, item ->
                    Tab(
                        selected = index == pagerState.currentPage,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(item.title) },
                        icon = {
                            Icon(
                                imageVector = if (index == pagerState.currentPage) {
                                    item.selectedIcon
                                } else item.unSelectedIcon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { index ->
                if (index == 0) {
                    // Pending Tasks
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(pending) { task ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(6.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Title: ${task.title}")
                                        Text("Description: ${task.description}")
                                        Text("Location: ${task.location}")
                                    }
                                    Button(onClick = { viewModel.updateTask(task.id) }) {
                                        Text("Task Done")
                                    }
                                }
                            }
                        }
                    }
                } else if (index == 1) {
                    // Completed Tasks
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(completed) { tasks ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(6.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Title: ${tasks.title}")
                                        Text("Description: ${tasks.description}")
                                        Text("Location: ${tasks.location}")
                                    }
                                    IconButton(onClick = { viewModel.delete(tasks.id) }) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
