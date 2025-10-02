package com.example.personaltaskmanager

import android.content.Context
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
        /*val sharedPreference=EncryptedSharedPreferences.create(
            context,
            "secure_pref",
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )*/
        val token = sharedPreferences.getString("access_token", null)
        val requestbuilder = chain.request().newBuilder()
            token?.let {
            requestbuilder.addHeader("Authorization", "Bearer $it")
        }
     /*   val username=sharedPreference.getString("username",null)
        val request=chain.request().newBuilder()
        username?.let {
            request.addHeader("username",it)
        }*/
        return chain.proceed(requestbuilder.build())



    }
}