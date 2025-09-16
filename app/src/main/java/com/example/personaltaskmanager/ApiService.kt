package com.example.personaltaskmanager

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("http://192.168.0.181:8000/")
    .addConverterFactory(GsonConverterFactory.create()).build()
val Services = retrofit.create(ApiServices::class.java)

interface ApiServices {
    @POST("tasks/create_task")
    suspend fun createTask(@Body request: Category)

    @GET("tasks/")
    suspend fun getTasks(): List<Task>

    @PUT("tasks/update_task/{task_id}")
    suspend fun markAsDone(
        @Path("task_id") id: Int,
        @Query("completed") completed: Boolean = true
    )


    @DELETE("tasks/delete_task/{task_id}")
    suspend fun deleteTask(
        @Path("task_id") id: Int
    )


    @POST("events/create_event/")

    suspend fun createEvent(@Body request: Events): retrofit2.Response<Events>

    @GET("events/")
    suspend fun getEvents(): List<Events>

    @DELETE("events/delete_event/{event_id}")
    suspend fun delete(@Path("event_id") id: Int)

    @PUT("events/update_event/{event_id}")
    suspend fun update(
        @Path("event_id") id: Int,
        @Body()request: Events):retrofit2.Response<Events>

}