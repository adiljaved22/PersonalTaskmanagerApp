package com.example.personaltaskmanager

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )


        val token = sharedPreferences.getString("access_token", null)
        Log.d("AuthInterceptor", "Token: $token")
        val request=chain.request()
        if (request.url.encodedPath.contains("/login") || (request.url.encodedPath.contains("/user/sign_up")))
            return chain.proceed(request)
        else {


            val requestbuilder = chain.request().newBuilder()
            token?.let {
                requestbuilder.addHeader("Authorization", "Bearer $it")
            }
            Log.d("AuthInterceptor", "Request: $requestbuilder")

            return chain.proceed(requestbuilder.build())


        }
    }
}