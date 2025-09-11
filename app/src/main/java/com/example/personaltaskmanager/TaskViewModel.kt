package com.example.personaltaskmanager

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
                /*val currentDate = sdf.parse(sdf.format(Date()))
                val coming = events.filter {
                    val eventDate = sdf.parse(it.event_date)
                    eventDate != null && !eventDate.before(currentDate)
                }
                val completed = events.filter {
                    val eventDate = sdf.parse(it.event_date)
                    eventDate != null && eventDate.before(currentDate)
                }*/
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


               val response= Services.update(eventId, eventName, eventLocation, eventDate, time)
                println("chabal,$response")
loadEvents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}





