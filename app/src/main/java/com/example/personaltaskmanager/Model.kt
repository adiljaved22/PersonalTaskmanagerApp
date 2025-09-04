package com.example.personaltaskmanager

data class Category(
    val title: String,
    val location: String
)
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val location: String,
    val isCompleted: Boolean
)