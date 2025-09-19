package com.example.personaltaskmanager

data class Category(
    val id: Int,
    val title: String,
    val description: String,
    val location: String,
    val completed: Boolean

)

data class Task(
    val id: Int=0,
    val firestoreId: String = "",
    val title: String="",
    val description: String="",
    val location: String="",
    val completed: Boolean=false
)

data class Events(
    val id: Int = 0,
    val firestoreId: String = "",
    val event_name: String? = "",
    val location: String? = "",
    val event_date: String? = "",
    val event_time: String? =""
)