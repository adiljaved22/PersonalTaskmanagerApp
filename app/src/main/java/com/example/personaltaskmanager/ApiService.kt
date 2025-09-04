    package com.example.personaltaskmanager
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory
    import retrofit2.http.Body
    import retrofit2.http.DELETE
    import retrofit2.http.GET
    import retrofit2.http.POST
    import retrofit2.http.PUT
    import retrofit2.http.Query

    private val retrofit = Retrofit.Builder().baseUrl("http://192.168.0.181:8000/tasks/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val Services = retrofit.create(ApiServices::class.java)

    interface ApiServices {
        @POST("create_task")
        suspend fun createTask(@Body request: Category)
        @GET(".")
        suspend fun getTasks(): List<Task>
        @PUT("update_task")
        suspend fun markAsDone(
            @Query("task_id") id: Int,
            @Query("completed") completed: Boolean = true
        )
        @DELETE("delete_task")
        suspend fun deleteTask(@Query("task_id") id: Int)

    }
