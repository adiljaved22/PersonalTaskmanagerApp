package com.example.personaltaskmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    onBack: () -> Unit,
    navController: NavController,
    viewModel: TaskViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }
    val coming by viewModel.comingEvent.collectAsState()
    val past by viewModel.pastEvent.collectAsState()
    val tabItems = listOf(
        TabItem(
            title = "Coming Events",
            unSelectedIcon = Icons.Outlined.Event,
            selectedIcon = Icons.Filled.Event
        ),
        TabItem(
            title = "Past Events",
            unSelectedIcon = Icons.Outlined.EventAvailable,
            selectedIcon = Icons.Filled.EventAvailable
        )
    )

    val pagerState = rememberPagerState(pageCount = { tabItems.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Events") },
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
                modifier = Modifier.fillMaxSize()
            ) { index ->
                if (index == 0) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(coming) { task ->
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
                                        Text("Title: ${task.event_name}")
                                        Text("Location: ${task.location}")
                                        Text("Date: ${task.event_date}")
                                        Text("Time: ${task.event_time}")
                                    }
                                }
                            }

                        }
                    }
                } else if (index == 1) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(past) { tasks ->
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
                                        Text("Title: ${tasks.event_name}")
                                        Text("Location: ${tasks.location}")
                                        Text("Date: ${tasks.event_date}")
                                        Text("Time: ${tasks.event_time}")
                                    }
                                }
                            }

                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = tabItems[index].title)
                }
            }
        }
    }

    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier.padding(20.dp),
            contentColor = Color.White,
            containerColor = colorResource(id = R.color.teal_700),
            onClick = {
                navController.navigate("AddEvents")
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}