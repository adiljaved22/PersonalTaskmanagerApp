package com.example.personaltaskmanager

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.String


/*class TaskViewModel : ViewModel() {
    private val _pendingTasks = MutableStateFlow<List<Task>>(emptyList())
    val pendingTask: StateFlow<List<Task>> = _pendingTasks
    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks: StateFlow<List<Task>> = _completedTasks


    private val _comingEvent = MutableStateFlow<List<Events>>(emptyList())
    val comingEvent: StateFlow<List<Events>> = _comingEvent

    private var _pastEvent = MutableStateFlow<List<Events>>(emptyList())
    val pastEvent: StateFlow<List<Events>> = _pastEvent


    init {
        loadTasks()
        loadEvents()
        startAutoRefreshEvents()
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
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentDateTime = Date()
                val events = Services.getEvents()


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
                try {
                    loadEvents()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(2_000) // 30 sec = 30,000 ms
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

    fun addEvent(idd: Int, name: String, location: String, date: String, time: String) {
        viewModelScope.launch {
            try {


                val newevent = Events(
                    id = idd,
                    event_name = name,
                    location = location,
                    event_date = date,
                    event_time = time, // ensure HH:mm:ss


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
                val response = Events(
                    id = eventId,
                    event_name = eventName,
                    location = eventLocation,
                    event_date = eventDate,
                    event_time = time,


                    )

                val Response = Services.update(eventId, response)

                if (Response.isSuccessful) {

                    loadEvents()
                } else {

                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun SignUp(Name: String, Email: String, Password: String) {
        viewModelScope.launch {
            try {


                val new = User(username = Name, email = Email, password = Password)
                val response = Services.signUp(new)
                if (response.isSuccessful) {
                    println("success")
                } else {
                    println("fail")
                }
            } catch (e: Exception) {
                println("$e")
            }
        }

    }

    fun login(email: String, password: String, context: Context) {
        viewModelScope.launch {
            val masterKey =
                MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            try {
                val response = Services.login(email, password)

                if (response.isSuccessful) {
                    val data = response.body()
                    val token = data?.token

                    if (!token.isNullOrEmpty()) {

                        sharedPreferences.edit().putString("access_token", token).apply()
                        Toast.makeText(context, "Login Successful ", Toast.LENGTH_LONG).show()
                    } else {

                        Toast.makeText(context, "Token Not Found ", Toast.LENGTH_LONG).show()

                    }
                } else {

                    Toast.makeText(context, "Invalid email or password ", Toast.LENGTH_LONG).show()

                }

            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }







}*/
class TaskViewModel(private val context: Context) : ViewModel() {

    // Retrofit services with AuthInterceptor
    private val services = RetrofitInstance.getApiServices(context)

    private val _pendingTasks = MutableStateFlow<List<Task>>(emptyList())
    val pendingTask: StateFlow<List<Task>> = _pendingTasks

    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks: StateFlow<List<Task>> = _completedTasks

    private val _comingEvent = MutableStateFlow<List<Events>>(emptyList())
    val comingEvent: StateFlow<List<Events>> = _comingEvent

    private val _pastEvent = MutableStateFlow<List<Events>>(emptyList())
    val pastEvent: StateFlow<List<Events>> = _pastEvent

    init {
        loadTasks()
        loadEvents()
        startAutoRefreshEvents()
    }

    fun loadTasks() {
        viewModelScope.launch {
            try {
                val tasks = services.getTasks()
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
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                val currentDateTime = Date()
                val events = services.getEvents()

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
                delay(2000) // 2 sec
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
                services.createTask(newTask)
                loadTasks()

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
                    event_time = time, // ensure HH:mm:ss


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

                    loadEvents()
                } else {

                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun SignUp(Name: String, Email: String, Password: String) {
        viewModelScope.launch {
            try {


                val new = User(username = Name, email = Email, password = Password)
                val response = services.signUp(new)
                if (response.isSuccessful) {
                    println("success")
                } else {
                    println("fail")
                }
            } catch (e: Exception) {
                println("$e")
            }
        }

    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = services.login(email, password)
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (!token.isNullOrEmpty()) {

                        val masterKey = MasterKey.Builder(context)
                            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                            .build()
                        val sharedPreferences = EncryptedSharedPreferences.create(
                            context,
                            "secure_prefs",
                            masterKey,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        )
                        sharedPreferences.edit().putString("access_token", token).apply()
                       Toast.makeText(
                            context,
                            "Login Successful",
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



