package com.example.personaltaskmanager

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.String


class TaskViewModel(application: Application) : AndroidViewModel(application) {


    private val context = getApplication<Application>().applicationContext
    private val services = RetrofitInstance.getApiServices(context)

    private val _pendingTasks = MutableStateFlow<List<Task>>(emptyList())
    val pendingTask: StateFlow<List<Task>> = _pendingTasks

    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks: StateFlow<List<Task>> = _completedTasks

    private val _comingEvent = MutableStateFlow<List<Events>>(emptyList())
    val comingEvent: StateFlow<List<Events>> = _comingEvent

    private val _pastEvent = MutableStateFlow<List<Events>>(emptyList())
    val pastEvent: StateFlow<List<Events>> = _pastEvent

    fun loadTasks() {
        viewModelScope.launch {
            val tasks = services.getTasks()
            try {


                _pendingTasks.value = tasks.filter { !it.completed }
                _completedTasks.value = tasks.filter { it.completed }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadEvents() {
        viewModelScope.launch {
            val events = services.getEvents()
            try {

                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val currentDateTime = Date()


                val coming = events.filter {
                    val dateTimeString = "${it.event_date} ${it.event_time}"
                    val eventDate = sdf.parse(dateTimeString)
                    eventDate != null && !eventDate.before(currentDateTime)
                }

                val completed = events.filter {
                    val dateTimeString = "${it.event_date} ${it.event_time}"
                    val eventDate = sdf.parse(dateTimeString)
                    eventDate != null && eventDate.before(currentDateTime)
                }
                _comingEvent.value = coming
                _pastEvent.value = completed
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun startAutoRefreshEvents() {
        viewModelScope.launch {
            while (true) {
                loadEvents()
                delay(2000)
            }
        }
    }
    fun addTask(id: Int, title: String, description: String, location: String, comp: Boolean) {
        viewModelScope.launch {
            try {

                val newTask = Category(id, title, description, location, comp)
                val response = services.createTask(newTask)
                if (response.isSuccessful) {
                    loadTasks()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addEvent(idd: Int, name: String, location: String, date: String, time: String) {
        viewModelScope.launch {
            try {


                val newevent = Events(
                    id = idd,
                    event_name = name,
                    location = location,
                    event_date = date,
                    event_time = time,


                    )
                val response = services.createEvent(newevent)
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
                services.markAsDone(taskId, true)
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
                services.deleteTask(taskId)
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
                services.delete(eventId)
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
                val response = Events(
                    id = eventId,
                    event_name = eventName,
                    location = eventLocation,
                    event_date = eventDate,
                    event_time = time,


                    )

                val Response = services.update(eventId, response)

                if (Response.isSuccessful) {
                    delay(1000)
                    loadEvents()
                } else {

                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun SignUp(
        image: MultipartBody.Part,
        username: RequestBody,
        email: RequestBody,
        password: RequestBody,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = services.SignUp(image, username, email, password)
                if (response.isSuccessful) {
                    Log.d("Signup", "Success: ${response.code()}")
                    onResult(true)
                } else {
                    Log.e("Signup", "Failed: ${response.code()} ${response.message()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("Signup", "Error: ${e.message}")
                onResult(false)
            }
        }
    }

    fun deviceToken(deviceToken: deviceToken) {
        viewModelScope.launch {
            val response = services.saveDeviceToken(deviceToken)
            if (response.isSuccessful) {
                Log.d("deviceToken", "$response")
            } else {
                Log.d("device token don't received", "${response.code()}")
            }
        }
    }

    fun login(
        email: String,
        password: String,
        navcontroller: NavController,
        onsuccess: () -> Unit
    ) {

        val context = getApplication<Application>().applicationContext
        viewModelScope.launch {
            try {
                val response = services.login(email, password)

                if (response.isSuccessful) {

                    Log.d("response", "$response")
                    val token = response.body()?.token
                    val username = response.body()?.user?.username
                    val email = response.body()?.user?.email
                    val profile_image = response.body()?.user?.profile_image
                    Log.d("token", "$token")
                    Log.d("username", "$username")
                    Log.d("email", "$email")
                    Log.d("profile_image", "$profile_image")
                    if (!token.isNullOrEmpty()) {
                        val sharedPreferences =
                            context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("access_token", token)
                            .putString("username", username)
                            .putString("email", email)
                            .putString("profile_image", profile_image)
                            .apply()

                        Toast.makeText(
                            context,
                            "Login Successful",
                            Toast.LENGTH_LONG
                        ).show()
                        navcontroller.navigate("home")
                        onsuccess()
                    } else {
                        Toast.makeText(
                            context,
                            "TOKEN NOT FOUND",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {

                    Toast.makeText(
                        context,
                        "Invalid email or password",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}