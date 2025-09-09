package com.example.personaltaskmanager

import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("http://192.168.0.181it:8000/")
    .addConverterFactory(GsonConverterFactory.create()).build()
val Services = retrofit.create(ApiServices::class.java)

    interface ApiServices {
        @POST("tasks/create_task")
        suspend fun createTask(@Body request: Category)
        @GET("tasks/")
        suspend fun getTasks(): List<Task>
        @PUT("tasks/update_task")
        suspend fun markAsDone(
            @Query("task_id") id: Int,
            @Query("completed") completed: Boolean = true
        )
        @DELETE("tasks/delete_task")
        suspend fun deleteTask(@Query("task_id") id: Int)


    @POST("events/create_event/")

        suspend fun createEvent(@Body request: Events): retrofit2.Response<Events>
/*        @Query("id") id :Int,
        @Query("event_name") eventName: String,
        @Query("location") eventLocation:String,
        @Query("event_date") eventDate: String,
        @Query("event_time") eventTime: String*/

    @GET("events/")
    suspend fun getEvents(): List<Events>

}
