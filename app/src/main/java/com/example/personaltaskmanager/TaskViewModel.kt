package com.example.personaltaskmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TaskViewModel : ViewModel() {
    private val _pendingTasks = MutableStateFlow<List<Task>>(emptyList())
    val pendingTask: StateFlow<List<Task>> = _pendingTasks
    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks: StateFlow<List<Task>> = _completedTasks

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            try {
                val tasks = Services.getTasks()
                _pendingTasks.value = tasks.filter { !it.isCompleted }
                _completedTasks.value = tasks.filter { it.isCompleted }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addTask(title: String, description: String, location: String) {
        viewModelScope.launch {
            try {

                val newTask = Category(title, description, location)
                Services.createTask(newTask)
                loadTasks()

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
                Services.deleteTask(taskId)
                loadTasks()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    }





