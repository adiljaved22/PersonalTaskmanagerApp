package com.example.personaltaskmanager

import android.content.Context
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
object RetrofitInstance {

    fun getApiServices(context: Context): ApiServices {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.181:9000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiServices::class.java)
    }


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
            @Body() request: Events
        ): retrofit2.Response<Events>

        @POST("user/sign_up")
        suspend fun signUp(@Body request: User): retrofit2.Response<User>

        @FormUrlEncoded
        @POST("login")
        suspend fun login(
            @Field("username") username: String,
            @Field("password") password: String
        ): Response<TokenResponse>

        data class TokenResponse(
            @SerializedName("access_token") val token: String,
            @SerializedName("token_type") val token_type: String
        )
    }
}
