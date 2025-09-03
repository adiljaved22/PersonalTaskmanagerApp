package com.example.personaltaskmanager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TaskViewModel : ViewModel() {
        private val _tasks = MutableStateFlow<List<Category>>(emptyList())
        val tasks: StateFlow<List<Category>> = _tasks

        fun addTask(title: String, location: String) {
            viewModelScope.launch {
                try {
                    Log.e("Add Task 11","$title $location")
                    val newTask = Category(title, location)
                    val response = Services.createTask(newTask)
                    _tasks.value = response.categories
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

