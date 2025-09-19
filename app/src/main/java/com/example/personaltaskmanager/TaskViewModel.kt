package com.example.personaltaskmanager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.delay
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


    init {
        loadTasks()
        loadEvents()
        startAutoRefreshEvents()
    }

    fun addTask(task: Task) {
        val db = FirebaseFirestore.getInstance()
        val id = db.collection("task").document()
        val taskId = task.copy(firestoreId = id.id)
        id.set(taskId)
            .addOnSuccessListener { println("Task Added") }
            .addOnFailureListener { e -> println("Task Failed,$e") }

    }

    fun addEventToFirestore(event: Events) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("events").document()
        val eventWithId = event.copy(firestoreId = docRef.id)
        docRef.set(eventWithId)
            .addOnSuccessListener {
                println("Event Added")
            }
            .addOnFailureListener { e ->
                println("Error: $e")
            }
    }

    fun loadTasks() {
        val db = FirebaseFirestore.getInstance()
        db.collection("task")
            .get()
            .addOnSuccessListener { result ->
                val tasks = result.map { doc ->
                    val task = doc.toObject(Task::class.java)
                    task.copy(firestoreId = doc.id)
                }
                _pendingTasks.value = tasks.filter { !it.completed }
                _completedTasks.value = tasks.filter { it.completed }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun loadEvents() {
        val db = FirebaseFirestore.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDateTime = Date()

        db.collection("events")
            .get()
            .addOnSuccessListener { result ->
                val events = result.map { doc ->
                    val event = doc.toObject(Events::class.java)
                    event.copy(firestoreId = doc.id)
                }

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
            }
            .addOnFailureListener { e ->
                println("Error getting events: $e")
            }
    }

    fun updateTask(firestoreId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("task").document(firestoreId)
            .update("completed", true)
            .addOnSuccessListener {
                Log.d("firestore", "Done")
                loadTasks()
            }.addOnFailureListener { e ->
                println("error, $e")
            }

    }

    fun updateEventInFirestore(event: Events) {
        val db = FirebaseFirestore.getInstance()
        if (event.firestoreId.isNotEmpty()) {
            db.collection("events").document(event.firestoreId)
                .set(event)
                .addOnSuccessListener {
                    println("Event Updated")
                }
                .addOnFailureListener { e ->
                    println("Error: $e")
                }
        } else {
            println("Error: No FirestoreId available")
        }
    }

    fun delete(firestoreId: String) {
        val db = FirebaseFirestore.getInstance()
        val taskRef = db.collection("task").document(firestoreId)
        taskRef.delete()
            .addOnSuccessListener {
                _completedTasks.value =
                    _completedTasks.value.filter { it.firestoreId != firestoreId }
                _pendingTasks.value = _pendingTasks.value.filter { it.firestoreId != firestoreId }
            }.addOnFailureListener { e ->
                println("Error deleting event: $e")
            }
    }

    fun deleteEventFromFirestore(firestoreId: String) {
        val db = FirebaseFirestore.getInstance()
        val eventRef = db.collection("events").document(firestoreId)

        eventRef.delete()
            .addOnSuccessListener {
                println("Event Deleted Successfully")

                _comingEvent.value = _comingEvent.value.filter { it.firestoreId != firestoreId }
                _pastEvent.value = _pastEvent.value.filter { it.firestoreId != firestoreId }
            }
            .addOnFailureListener { e ->
                println("Error deleting event: $e")
            }
    }

    private fun startAutoRefreshEvents() {
        viewModelScope.launch {
            while (true) {
                try {
                    loadEvents()
                    loadTasks()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(30_000)
            }
        }
    }

    fun getEventByFirestoreId(firestoreId: String): Events? {
        return _comingEvent.value.find { it.firestoreId == firestoreId } ?: _pastEvent.value.find { it.firestoreId == firestoreId }
    }
    /*  fun update(task: Task) {
        val db = FirebaseFirestore.getInstance()
        if (task.firestoreId.isNotEmpty()) {
            db.collection("events").document(task.firestoreId)
                .set(task)
                .addOnSuccessListener {
                    println("Event Updated")
                }
                .addOnFailureListener { e ->
                    println("Error: $e")
                }
        } else {
            println("Error: No FirestoreId available")
        }
    }*/
    /*  fun loadTasks() {
          viewModelScope.launch {
              try {
                  val tasks = Services.getTasks()
                  _pendingTasks.value = tasks.filter { !it.completed }
                  _completedTasks.value = tasks.filter { it.completed }
              } catch (e: Exception) {
                  e.printStackTrace()
              }
          }
      }*/
    /*  fun addTask(id: Int, title: String, description: String, location: String, comp: Boolean) {

          viewModelScope.launch {
              try {

                  val newTask = Category(id, title, description, location, comp)
                  Services.createTask(newTask)
                  loadTasks()

              } catch (e: Exception) {
                  e.printStackTrace()
              }
          }
      }*/

    /*  fun updateTask(taskId: Int) {
      viewModelScope.launch {
          try {
              Services.markAsDone(taskId, true)
              loadTasks()
          } catch (e: Exception) {
              e.printStackTrace()
          }
      }
  }*/
}