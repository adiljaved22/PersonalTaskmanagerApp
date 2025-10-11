
package com.example.personaltaskmanager

data class Category(
    val id: Int,
    val title: String,
    val description: String,
    val location: String, val completed: Boolean

)

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val location: String,
    val completed: Boolean
)

data class Events(
    val id: Int,
    val event_name: String,
    val location: String,
    val event_date: String,
    val event_time: String
)
data class User(

    val username:String,
    val email:String,
    val password:String,
    val profile_image: String? = null
)
data class deviceToken(
    val token:String?=null
)

