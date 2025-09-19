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
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DoneOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    var dialogBox by remember { mutableStateOf(false) }
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
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.White,
                    containerColor = colorResource(id = R.color.teal_700)
                ),
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
                   /* val context = LocalContext.current
                    val cm = context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE)
                            as android.net.ConnectivityManager
                    val networkInfo = cm.activeNetworkInfo
                    if (networkInfo != null && networkInfo.isConnected) {*/


                        if (pending.isEmpty()) {
                            Text("\t Nothing to display yet.Add some Tasks!")
                        } else {
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
                                            IconButton(onClick = { viewModel.updateTask(task.firestoreId) }) {
                                                Icon(
                                                    imageVector = Icons.Rounded.DoneOutline,
                                                    contentDescription = null
                                                )
                                            }
                                            if (dialogBox) {
                                                AlertDialog(
                                                    onDismissRequest = { dialogBox = false },
                                                    title = { Text("Are you want To delete this item? ") },

                                                    confirmButton = {
                                                        Button(onClick = {
                                                            viewModel.delete(task.firestoreId)


                                                            dialogBox = false

                                                        }) {
                                                            Text("Confirm")
                                                        }
                                                    },

                                                    dismissButton = {
                                                        Button(onClick = { dialogBox = false }) {
                                                            Text("Dismiss")
                                                        }
                                                    }
                                                )
                                            }

                                            IconButton(onClick = {

                                                dialogBox = true
                                            }) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = null
                                                )
                                            }

                                        }
                                    }
                                }
                            }
                        }
                   /* } else {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No internet Available",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }*/

                } else if (index == 1) {
                    val context = LocalContext.current
                    /*val cm = context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE)
                            as android.net.ConnectivityManager
                    val networkInfo = cm.activeNetworkInfo
                    if (networkInfo != null && networkInfo.isConnected) {*/
                        if (completed.isEmpty()) {
                            Text("\t Nothing to display yet.Add some Tasks!")
                        } else {
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
                                            if (dialogBox) {
                                                AlertDialog(
                                                    onDismissRequest = { dialogBox = false },
                                                    title = { Text("Are you want To delete this item? ") },

                                                    confirmButton = {
                                                        Button(onClick = {
                                                            viewModel.delete(tasks.firestoreId)


                                                            dialogBox = false

                                                        }) {
                                                            Text("Confirm")
                                                        }
                                                    },

                                                    dismissButton = {
                                                        Button(onClick = { dialogBox = false }) {
                                                            Text("Dismiss")
                                                        }
                                                    }
                                                )
                                            }

                                            IconButton(onClick = {

                                                dialogBox = true
                                            }) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                  /*  } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No internet Available",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }*/
                }
            }
        }
    }
}