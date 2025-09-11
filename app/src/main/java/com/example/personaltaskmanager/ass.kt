/*
package com.example.personaltaskmanager

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.sql.Time
import java.util.Date

private val retrofit = Retrofit.Builder().baseUrl("http://192.168.0.181:8000/")
    .addConverterFactory(GsonConverterFactory.create()).build()
val Services = retrofit.create(ApiServices::class.java)

interface ApiServices {
    @POST("tasks/create_task")
    suspend fun createTask(@Body request: Category)

    @GET("tasks/")
    suspend fun getTasks(): List<Task>

    @PUT("tasks/update_task/{task_id}")
    suspend fun markAsDone(
        @Path("task_id") id: Int,
        @Query("completed") completed: Boolean = true
    )


    @DELETE("tasks/delete_task/{task_id}")
    suspend fun deleteTask(
        @Path("task_id") id: Int
    )


    @POST("events/create_event/")

    suspend fun createEvent(@Body request: Events): retrofit2.Response<Events>

    @GET("events/")
    suspend fun getEvents(): List<Events>

    @DELETE("events/delete_event/{event_id}")
    suspend fun delete(@Path("event_id") id: Int)

    @PUT("events/update_event/{event_id}")
    suspend fun update(
        @Path("event_id") id: Int,
        @Query("event_name") name: String,
        @Query("location") location: String,
        @Query("event_date") date: Int,
        @Query("event_time") time: Int,

        )

}



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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Edit(
    EventsToBeEdit: Events,
    onBack: () -> Unit,
    viewModel: TaskViewModel = viewModel()
) {
    var name by remember { mutableStateOf(EventsToBeEdit.event_name) }
    var location by remember { mutableStateOf(EventsToBeEdit.location) }
    var date by remember { mutableStateOf(EventsToBeEdit.event_date) }
    var time by remember { mutableStateOf(EventsToBeEdit.event_time) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
}package com.example.personaltaskmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.String


class TaskViewModel : ViewModel() {
    private val _pendingTasks = MutableStateFlow<List<Task>>(emptyList())
    val pendingTask: StateFlow<List<Task>> = _pendingTasks
    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks: StateFlow<List<Task>> = _completedTasks


    private val _comingEvent = MutableStateFlow<List<Events>>(emptyList())
    val comingEvent: StateFlow<List<Events>> = _comingEvent

    private var _pastEvent = MutableStateFlow<List<Events>>(emptyList())
    val pastEvent: StateFlow<List<Events>> = _pastEvent
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    init {
        loadTasks()
        loadEvents()
    }

    fun loadTasks() {
        viewModelScope.launch {
            try {
                val tasks = Services.getTasks()
                _pendingTasks.value = tasks.filter { !it.completed }
                _completedTasks.value = tasks.filter { it.completed }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadEvents() {
        viewModelScope.launch {
            try {
                val events = Services.getEvents()
                val currentDate = sdf.parse(sdf.format(Date()))
                val coming = events.filter {
                    val eventDate = sdf.parse(it.event_date)
                    eventDate != null && !eventDate.before(currentDate)
                }
                val completed = events.filter {
                    val eventDate = sdf.parse(it.event_date)
                    eventDate != null && eventDate.before(currentDate)
                }
                _comingEvent.value = coming
                _pastEvent.value = completed
                */
/*val currentDate = sdf.parse(sdf.format(Date()))
                val coming = events.filter {
                    val eventDate = sdf.parse(it.event_date)
                    eventDate != null && !eventDate.before(currentDate)
                }
                val completed = events.filter {
                    val eventDate = sdf.parse(it.event_date)
                    eventDate != null && eventDate.before(currentDate)
                }*//*

                _comingEvent.value = coming
                _pastEvent.value = completed
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getEventById(id: Int): Events? {
        return _comingEvent.value.find { it.id == id } ?: _pastEvent.value.find { it.id == id }
    }

    fun addTask(id: Int, title: String, description: String, location: String, comp: Boolean) {
        viewModelScope.launch {
            try {

                val newTask = Category(id, title, description, location, comp)
                Services.createTask(newTask)
                loadTasks()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addEvent(idd: Int, name: String, location: String, date: String, hour: Int, minute: Int) {
        viewModelScope.launch {
            try {

                val formattedTime = String.format("%02d:%02d:00", hour, minute) // HH:mm:ss
                val newevent = Events(
                    id = idd,
                    event_name = name,
                    location = location,
                    event_date = date,
                    event_time = formattedTime, // ensure HH:mm:ss


                )
                val response = Services.createEvent(newevent)
                if (response.isSuccessful) {
                    loadEvents()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTask(taskId: Int) {
        viewModelScope.launch {
            try {
                Services.markAsDone(taskId, true)
                loadTasks()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun delete(taskId: Int) {
        viewModelScope.launch {
            try {
                _completedTasks.value = _completedTasks.value.filter { it.id != taskId }
                _pendingTasks.value = _pendingTasks.value.filter { it.id != taskId }
                Services.deleteTask(taskId)
                loadTasks()
            } catch (e: Exception) {
                e.printStackTrace()
                loadTasks()
            }
        }
    }

    fun deleteEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                _pastEvent.value = _pastEvent.value.filter { it.id != eventId }
                _comingEvent.value = _comingEvent.value.filter { it.id != eventId }
                Services.delete(eventId)
                loadEvents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun update(

        eventId: Int,
        eventName: String,
        eventLocation: String,
        eventDate: String,
        time: String
    ) {

        viewModelScope.launch {
            try {


                Services.update(eventId, eventName, eventLocation, eventDate, time)
loadEvents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
package com.example.personaltaskmanager

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation(viewModel: TaskViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") {
            HomeScreen(
                NavigateToTask = { navController.navigate("Task") },
                NavigateToEvents = { navController.navigate("Events") }

            )
        }
        composable("Task")
        {
            TaskScreen(
                navController = navController,
                onBack = {
                    navController.popBackStack() })
        }
        composable("Events")
        {
            EventsScreen(
                navController = navController, onBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(
            "Edit/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { entry ->
            val taskId = entry.arguments!!.getInt("taskId")
            val event = viewModel.getEventById(taskId)
            event?.let {
                Edit(
                    EventsToBeEdit = it,
                    onBack = { navController.popBackStack() },

                    )
            }
        }
        composable("AddTask")
        {
            AddTask(
                onBack = {
                    navController.navigate("Home")
                    {
                        popUpTo(0)
                        {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onBackClick = { navController.popBackStack() })
        }
        composable("AddEvents") {
            AddEvents(
                onBack = {
                    navController.navigate("Home")

                    {
                        popUpTo(0)
                        {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }

                },
                onBackCLick = { navController.popBackStack() }
            )
        }

    }
}





*/
