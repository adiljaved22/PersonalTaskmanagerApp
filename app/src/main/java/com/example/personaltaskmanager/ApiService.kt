package com.example.personaltaskmanager

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private val retrofit = Retrofit.Builder().baseUrl("http://192.168.0.181:8000/tasks/")
    .addConverterFactory(GsonConverterFactory.create()).build()
val Services = retrofit.create(ApiServices::class.java)

interface ApiServices {
@POST("create_task")
    suspend fun createTask(@Body category: Category): CategoryResponse
}
