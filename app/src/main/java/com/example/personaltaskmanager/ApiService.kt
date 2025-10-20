package com.example.personaltaskmanager

import android.content.Context
import android.media.Image
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.internal.platform.android.AndroidLogHandler.setLevel
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.0.181:8000/"
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    fun getApiServices(context: Context): ApiServices {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context)).addInterceptor(loggingInterceptor).build()


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiServices::class.java)
    }


    interface ApiServices {
        @POST("user/save-device-token")
        suspend fun saveDeviceToken(@Body request: deviceToken): retrofit2.Response<deviceToken>

        @POST("tasks/create_task")
        suspend fun createTask(@Body request: Category): retrofit2.Response<Task>

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

        @POST("events/create_event")

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


        @FormUrlEncoded
        @POST("login")
        suspend fun login(
            @Field("username") username: String,
            @Field("password") password: String
        ): Response<TokenResponse>

        data class TokenResponse(
            @SerializedName("access_token") val token: String,
            @SerializedName("token_type") val token_type: String,
            @SerializedName("user") val user: UserData?


        )

        data class UserData(
            @SerializedName("username") val username: String,
            @SerializedName("email") val email: String,
            @SerializedName("profile_image") val profile_image: String
        )

        @Multipart
        @POST("user/sign_up")
        suspend fun SignUp(
            @Part profile_image: MultipartBody.Part,
            @Part("username") username: RequestBody,
            @Part("email") email: RequestBody,
            @Part("password") password: RequestBody
        ): Response<User>
    }
}